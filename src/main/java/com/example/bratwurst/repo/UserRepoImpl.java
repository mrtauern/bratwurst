package com.example.bratwurst.repo;

import org.springframework.stereotype.Repository;

import java.util.logging.Logger;

@Repository
public class UserRepoImpl implements UserRepo {

    Logger log = Logger.getLogger(UserRepoImpl.class.getName());
}
