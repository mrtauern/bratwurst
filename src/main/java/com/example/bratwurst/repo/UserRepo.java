package com.example.bratwurst.repo;

import com.example.bratwurst.ViewModel.UserViewModel;
import com.example.bratwurst.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo {

    void insertUser(UserViewModel u);

    List<User> getAllUsers();

    User getUserById(int id);

    UserViewModel login(String username, String password);

    void deleteUser(int id);
}
