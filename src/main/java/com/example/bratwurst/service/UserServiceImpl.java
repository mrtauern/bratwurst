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

    @Override
    public User addUser(User user, String confirm_password) {

        User matching_user = userRepo.findLogin(user.getUsername(), user.getEmail());

        String current_pass = user.getPassword();

        // if the two passwords match
        if (current_pass.equals(confirm_password)){

            if (matching_user == null){
                return userRepo.addUser(user);
            }

            // if there is a match on email
            if (user.getEmail().equals(matching_user.getEmail())){

                matching_user.setUsername(null);

                return matching_user;
            // if there is a match on username
            } else if(user.getUsername().toLowerCase().equals(matching_user.getUsername().toLowerCase())) {

                matching_user.setEmail(null);

                return matching_user;
            }

            return null;

        } else{

            return null;
        }

    }
}
