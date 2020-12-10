package com.example.bratwurst.repo;

import com.amazonaws.services.sns.AmazonSNS;
import com.example.bratwurst.hashfunctions.HashFunctions;
import com.example.bratwurst.model.FriendsViewModel;
import com.example.bratwurst.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    public List<FriendsViewModel> getUsers(int id){

        List<FriendsViewModel> userList = new ArrayList<>();


        String sql = "SELECT u.id, u.username, u.profile_picture, u.last_name, u.first_name, u.country, u.email, f.user1, f.user2, f.accepted FROM users AS u " +
                "LEFT OUTER JOIN friends AS f on f.user1 = u.id";


        return this.jdbc.query(sql, rs ->
        {
            while (rs.next()) {
                int userId = rs.getInt("id");
                String username = rs.getString("username");

                String profile_picture = rs.getString("profile_picture");
                int user1 = rs.getInt("user1");
                int user2 = rs.getInt("user2");
                boolean accepted = rs.getBoolean("accepted");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String country = rs.getString("country");
                String email = rs.getString("email");

                FriendsViewModel fvm = new FriendsViewModel(userId, username, profile_picture, user1, user2, accepted, first_name, last_name, country, email);


                userList.add(fvm);
            }
            return userList;
        });
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

    public void friendRequest(int userId, int receiverId){

        String sql = "INSERT INTO friends VALUES(?, ?, 0)";

        jdbc.update(sql, userId, receiverId);
    }

    public List<User> notifications(int id){

        List<User> userList = new ArrayList<User>();

        String sql = "SELECT * FROM friends WHERE user2 = ? AND accepted = 0";

        return this.jdbc.query(sql, rs ->
        {
            while (rs.next()) {
                int userId = rs.getInt("user1");

                User u = new User();
                u.setId(userId);

                userList.add(u);
            }
            return userList;
        },id);

    }

    public void acceptRequest(int receiverId, int userId) {

        String sql = "UPDATE friends SET accepted = 1 WHERE user1 = ? AND user2 = ?";

        jdbc.update(sql, receiverId, userId);
    }
    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbc.update(sql, id);

    }
}
