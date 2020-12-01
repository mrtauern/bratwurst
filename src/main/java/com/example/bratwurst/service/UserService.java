package com.example.bratwurst.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.example.bratwurst.model.User;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    boolean validateAuthCode(int inputCode);
    void publishMessage(String email) throws JSONException;
    void subscribeToTopic(String email) throws JSONException;
    void setPolicyFilter(String email) throws JSONException;
    User getLogin(String username, String password);
    List<User> getUsers(int id);
    User addUser(User user, String confirm_password);
    boolean passwordStrong(String password);
    User getUserById(int id);

}
