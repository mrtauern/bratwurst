package com.example.bratwurst.service;

import com.example.bratwurst.ViewModel.UserViewModel;
import com.example.bratwurst.model.User;
import com.example.bratwurst.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Override
    public void insertUser(UserViewModel u) {
        userRepo.insertUser(u);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.getAllUsers();
    }

    @Override
    public User getUserById(int id) {
        return userRepo.getUserById(id);
    }

    @Override
    public UserViewModel login(String username, String password) {
        return userRepo.login(username, password);
    }

    @Override
    public void deleteUser(int id) {
        userRepo.deleteUser(id);
    }
}
