package com.example.bratwurst.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/account_settings")
    public String account_setting(){

        return "account_settings";
    }
}
