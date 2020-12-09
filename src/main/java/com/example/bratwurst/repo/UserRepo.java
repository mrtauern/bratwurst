package com.example.bratwurst.repo;

import com.example.bratwurst.model.FriendsViewModel;
import com.example.bratwurst.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo {

    User getLogin(String username, String password);
    List<FriendsViewModel> getUsers(int id);
    User addUser(User user);
    User findLogin(String username, String email);
    User getUserById(int id);
    void friendRequest(int userId, int receiverId);
    List<User> notifications(int id);
    void acceptRequest(int receiverId, int userId);
}
