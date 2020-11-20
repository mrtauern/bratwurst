package com.example.bratwurst.repo;

import com.example.bratwurst.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo {

    User getLogin(String username, String password);
}
