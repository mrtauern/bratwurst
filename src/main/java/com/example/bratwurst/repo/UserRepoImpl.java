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
}
