package com.example.bratwurst.repo;

import com.example.bratwurst.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

@Repository
public class UserRepoImpl implements UserRepo {

    Logger log = Logger.getLogger(UserRepoImpl.class.getName());

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbc;

    public User getLogin(String username, String password){
        String sql = "SELECT id FROM users WHERE username = ? and password = ?";
        User u = this.jdbc.query(sql, resultSet -> {
              User user = new User();
              while (resultSet.next()){
                  user.setId(resultSet.getInt("id"));
                  return user;
              }
            return null;
        }, username, password);
        return u;
    }

    @Override
    public User addUser(User user) {

        String sql = "INSERT INTO users Values (default, ?, ?, 'test', ?, ?, ?, ?, ?, ?, ?, null)";
        jdbc.update(sql, user.getUsername(), user.getPassword(), user.getFirst_name(),
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


}
