package com.example.bratwurst.repo;

import com.example.bratwurst.HashFunction.HashFunctions;
import com.example.bratwurst.ViewModel.UserViewModel;
import com.example.bratwurst.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
public class UserRepoImpl extends JdbcDaoSupport implements UserRepo {

    Logger log = Logger.getLogger(UserRepoImpl.class.getName());

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbc;

    HashFunctions h = new HashFunctions();
    //byte[] salt = ("Maddi").getBytes();

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    public void insertUser(UserViewModel u) {

        byte[] salt = h.createSalt();
        String hash = h.getHash(u.getPassword().getBytes(), "SHA-1", salt);

        String generatedString = new String(salt, Charset.forName("UTF-8"));

        User user = new User();

        user.setFirst_name(u.getFirst_name());
        user.setLast_name(u.getLast_name());
        user.setAge(u.getAge());
        user.setGender(u.getGender());
        user.setUsername(u.getUsername());
        user.setSalt(salt.toString());
        user.setPassword(hash);
        user.setEmail(u.getEmail());
        user.setProfile_picture(u.getProfile_picture());

        String sql = "INSERT INTO User (first_name, last_name, age, gender, email, username, salt, " +
                        "password, profile_picture) "
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        getJdbcTemplate().update(sql, new Object[]{
                user.getFirst_name(), user.getLast_name(), user.getAge(), user.getGender(), user.getEmail(),
                user.getUsername(), generatedString, hash, user.getProfile_picture()
        });
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM User";

        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
        List<User> result = new ArrayList<User>();

        for (Map<String, Object> row : rows) {
            User u = new User();
            u.setIduser((int) row.get("iduser"));
            u.setFirst_name((String) row.get("first_name"));
            u.setLast_name((String) row.get("last_name"));
            u.setAge((int) row.get("age"));
            u.setGender((String) row.get("gender"));
            u.setEmail((String) row.get("email"));
            u.setUsername((String) row.get("username"));
            u.setSalt((String) row.get("salt"));
            u.setPassword((String) row.get("password"));
            u.setProfile_picture((String) row.get("profile_picture"));

            result.add(u);
        }

        return result;
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM User WHERE iduser = ?";

        return (User) getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<User>() {
            public User mapRow(ResultSet rs, int rwNumber) throws SQLException {
                User u = new User();

                u.setIduser(rs.getInt("iduser"));
                u.setFirst_name(rs.getString("first_name"));
                u.setLast_name(rs.getString("last_name"));
                u.setAge(rs.getInt("age"));
                u.setGender(rs.getString("gender"));
                u.setEmail(rs.getString("email"));
                u.setUsername(rs.getString("username"));
                u.setSalt(rs.getString("salt"));
                u.setPassword(rs.getString("password"));
                u.setProfile_picture(rs.getString("profile_picture"));

                return u;
            }
        });
    }

    @Override
    public UserViewModel login(String username, String password) {
        String sql = "SELECT * FROM User WHERE username = ?";


        User u = this.jdbc.query(sql, new ResultSetExtractor<User>() {
            @Override
            public User extractData(ResultSet rs) throws SQLException, DataAccessException {
                String first_name, last_name, gender, email, userName, pass, salt, profile_picture;
                int id, age;
                User user = new User();

                while (rs.next()) {
                    id = rs.getInt("iduser");
                    first_name = rs.getString("first_name");
                    last_name = rs.getString("last_name");
                    age = rs.getInt("age");
                    gender = rs.getString("gender");
                    userName = rs.getString("username");
                    salt = rs.getString("salt");
                    pass = rs.getString("password");
                    email = rs.getString("email");
                    profile_picture = rs.getString("profile_picture");

                    user.setIduser(id);
                    user.setFirst_name(first_name);
                    user.setLast_name(last_name);
                    user.setAge(age);
                    user.setGender(gender);
                    user.setUsername(userName);
                    user.setSalt(salt);
                    user.setPassword(pass);
                    user.setEmail(email);
                    user.setProfile_picture(profile_picture);
                }
                return user;
            }
        },username);

        if (u.getUsername() == null && u.getPassword() == null) {
            return null;
        }

        UserViewModel uvm = new UserViewModel();

        uvm.setFirst_name(u.getFirst_name());
        uvm.setLast_name(u.getLast_name());
        uvm.setAge(u.getAge());
        uvm.setGender(u.getGender());
        uvm.setEmail(u.getEmail());
        uvm.setUsername(u.getUsername());
        uvm.setProfile_picture(u.getProfile_picture());

        String hash = h.getHash(password.getBytes(), "SHA-1", u.getSalt().getBytes());

        System.out.println("HASH1: " + u.getPassword());
        System.out.println("HASH2: " + hash);

        if (u.getPassword().equals(hash)) {


            uvm.setPassword(u.getPassword());
        } else {

            uvm.setUsername(null);
            uvm.setPassword(null);
        }

        return uvm;
    }

    @Override
    public void deleteUser(int id) {
        String sql = "DELETE FROM User WHERE idusers = ?";

        getJdbcTemplate().update(sql, id);
    }
}
