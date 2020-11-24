package com.example.bratwurst.service;

import com.example.bratwurst.ViewModel.UserViewModel;
import com.example.bratwurst.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    void insertUser(UserViewModel u);

    List<User> getAllUsers();

    User getUserById(int id);

    UserViewModel login(String username, String password);

    void deleteUser(int id);
}
