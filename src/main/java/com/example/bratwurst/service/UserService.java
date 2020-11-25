package com.example.bratwurst.service;

import com.example.bratwurst.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getLogin(String username, String password);

    User addUser(User user, String confirm_password);

}
