package com.example.bratwurst.service;

import com.example.bratwurst.repo.HomeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class HomeServiceImpl {

    Logger log = Logger.getLogger(HomeServiceImpl.class.getName());

    @Autowired
    HomeRepo homeRepo;
}
