package com.example.bratwurst.repo;

import com.amazonaws.services.sns.AmazonSNS;
import com.example.bratwurst.hashfunctions.HashFunctions;
import com.example.bratwurst.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class UserRepoImpl implements UserRepo {

    Logger log = Logger.getLogger(UserRepoImpl.class.getName());

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbc;

    HashFunctions h = new HashFunctions();
    //byte[] salt = ("Maddi").getBytes();

    public User getLogin(String username, String password){

        //Get the salt to hash with the password
        String checkSalt = "SELECT password, salt FROM users WHERE username = ?";

        User saltUser = this.jdbc.query(checkSalt, new ResultSetExtractor<User>() {
            @Override
            public User extractData(ResultSet rs) throws SQLException, DataAccessException {
                User user = new User();
                while (rs.next()) {
                    user.setPassword(rs.getString("password"));
                    user.setSalt(rs.getString("salt"));
                    return user;
                }
                return null;
            }
        },username);

        if(saltUser == null){
            return null;
        }
        password = password + "" + h.PEBER;

        String hash = h.getHash(password.getBytes(), "SHA-512", saltUser.getSalt().getBytes());

        //Check if the user exist in the database
         String sql = "SELECT id, email FROM users WHERE username = ? AND password = ?";

        User u = this.jdbc.query(sql, resultSet -> {
              User user = new User();
              while (resultSet.next()){
                  user.setId(resultSet.getInt("id"));
                  user.setEmail(resultSet.getString("email"));
                  return user;
              }
            return null;
        }, username, hash);
        return u;
    }

    public List<User> getUsers(int id){

        List<User> userList = new ArrayList<User>();

        String sql = "SELECT id, username, first_name, last_name, country FROM users " +
                "WHERE id != ?";

        return this.jdbc.query(sql, rs ->
        {
            while (rs.next()) {
                int userId = rs.getInt("id");
                String username = rs.getString("username");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String country = rs.getString("country");

                User u = new User(username, first_name, last_name, country);
                u.setId(userId);

                userList.add(u);
            }
            return userList;
        },id);
    }

    @Override
    public User addUser(User user) {

        user.setPassword(user.getPassword() + "" + h.PEBER);
        byte[] salt = h.createSalt();
        String hash = h.getHash(user.getPassword().getBytes(), "SHA-512", salt);

        String generatedString = new String(salt, Charset.forName("UTF-8"));

        String sql = "INSERT INTO users Values (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, null)";
        jdbc.update(sql, user.getUsername(), hash, generatedString, user.getFirst_name(),
                         user.getLast_name(), user.getCountry(), user.getCity(), user.getAge(),
                         user.getEmail(), user.isGender());
        return user;
    }

    @Override
    public User findLogin(String username, String email) {
        String sql = "SELECT username, email FROM users WHERE username = ? or email = ?";
        User u = this.jdbc.query(sql, resultSet -> {
            User user = new User();
            while (resultSet.next()){
                user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("username"));
                return user;
            }
            return null;
        }, username, email);
        return u;
    }

    //Get User information for homepage
    @Override
    public User getUserById(int id) {

        String sql = "SELECT username, first_name, last_name, country, city, age, gender, profile_picture " +
                "FROM users WHERE id = ?";

        RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
        User user = jdbc.queryForObject(sql, rowMapper, id);
        return user;
    }
}
