package com.example.bratwurst.service;

import com.example.bratwurst.model.User;
import com.example.bratwurst.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public List<User> getUsers() {
        return userRepo.getUsers();
    }


    @Override
    public User addUser(User user, String confirm_password) {

        User matching_user = userRepo.findLogin(user.getUsername(), user.getEmail());

        String current_pass = user.getPassword();

        boolean strongPassword = passwordStrong(user.getPassword());

        if (strongPassword != true){
            return null;
        }

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

    @Override
    public boolean passwordStrong(String password) {

        boolean haveUpper = false;
        boolean haveLower = false;
        boolean length = false;
        boolean specialCharacter = false;

        // Checks for lowercase and upercase
        for( int i = 0; i< password.length(); i++){

            char ch = password.charAt(i);

            if (Character.isUpperCase(ch)){
                haveUpper = true;
            }else if(Character.isLowerCase(ch)){
                haveLower = true;
            }
        }
        // Checks for length
        if (password.length() > 7) {
            length = true;
        }

        // Checks for special character
        Pattern p = Pattern.compile("[A-Za-z0-9]");
        Matcher m = p.matcher(password);
        boolean b = m.find();
        if (b){
            specialCharacter = true;
        }

        if (haveUpper && haveLower && length && specialCharacter){
            return true;
        }else{

            return false;
        }
    }
}
