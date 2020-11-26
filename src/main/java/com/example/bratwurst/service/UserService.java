package com.example.bratwurst.service;

import com.example.bratwurst.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    User getLogin(String username, String password);
    List<User> getUsers();
    User addUser(User user, String confirm_password);

}
