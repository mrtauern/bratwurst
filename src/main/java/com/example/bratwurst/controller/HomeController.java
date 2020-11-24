package com.example.bratwurst.controller;

import com.example.bratwurst.model.User;
import com.example.bratwurst.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.logging.Logger;

@Controller
public class HomeController {

    public HomeController() {
    }

    Logger log = Logger.getLogger(HomeController.class.getName());

    @Autowired
    UserService userService;

    @GetMapping("/{username}/{password}")
    public String index(@PathVariable String username,@PathVariable String password){
        log.info("index called");
        User user = userService.getLogin(username,password);
        if (user == null){
            System.out.println("error");
        }
        return "index";
    }

    @GetMapping("/home")
    public String home(){

        return "home";
    }

    @GetMapping("/messages")
    public String messages(){

        return "messages";
    }

    @GetMapping("/notifications")
    public String notifications(){

        return "notifications";
    }
}
