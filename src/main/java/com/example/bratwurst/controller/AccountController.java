package com.example.bratwurst.controller;

import com.example.bratwurst.model.User;
import com.example.bratwurst.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

@Controller
public class AccountController {

    @Autowired
    UserService userService;

    Logger log = Logger.getLogger(HomeController.class.getName());

    @CrossOrigin()
    @GetMapping("/account_settings")
    public String account_setting(HttpSession session, Model model){

        if (session.getAttribute("login") != null){

            User u = (User)session.getAttribute("login");

            int notifications = (userService.notifications(u.getId())).size();
            model.addAttribute("notifications", notifications);

            model.addAttribute("user", userService.getUserById(u.getId()));

            return "account_settings";
        }else {

            model.addAttribute("notLoggedIn", "notLoggedIn");
            return "index";
        }
    }
}
