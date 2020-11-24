package com.example.bratwurst.service;

import com.example.bratwurst.model.User;
import com.example.bratwurst.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService {

    Logger log = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    UserRepo userRepo;

    @Override
    public User getLogin(String username, String password) {
        User user  = userRepo.getLogin(username, password);
        if (user == null){
            return null;
        }
        else{
            return user;
        }
    }
}
