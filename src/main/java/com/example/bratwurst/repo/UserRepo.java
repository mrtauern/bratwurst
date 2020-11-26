package com.example.bratwurst.repo;

import com.example.bratwurst.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo {

    User getLogin(String username, String password);
    List<User> getUsers();
    User addUser(User user);
    User findLogin(String username, String email);
}
