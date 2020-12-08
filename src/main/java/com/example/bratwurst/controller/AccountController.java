package com.example.bratwurst.controller;

import com.example.bratwurst.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class AccountController {

    @Autowired
    UserService userService;

    Logger log = Logger.getLogger(HomeController.class.getName());

    @CrossOrigin()
    @GetMapping("/account_settings")
    public String account_setting(){

        return "account_settings";
    }
}
