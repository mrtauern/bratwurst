package com.example.bratwurst.repo;

import com.example.bratwurst.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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

    public List<User> getUsers(){

        List<User> userList = new ArrayList<User>();

        String sql = "SELECT username, first_name, last_name, country FROM users";

        return this.jdbc.query(sql, new ResultSetExtractor<java.util.List<User>>() {

            @Override
            public java.util.List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {

                while (rs.next()) {
                    String username = rs.getString("username");
                    String first_name = rs.getString("first_name");
                    String last_name = rs.getString("last_name");
                    String country = rs.getString("country");

                    User u = new User(username, first_name, last_name, country);

                    userList.add(u);
                }
                return userList;
            }
        });
    }
}
