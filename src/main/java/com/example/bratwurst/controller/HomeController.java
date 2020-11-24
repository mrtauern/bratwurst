package com.example.bratwurst.controller;

import com.example.bratwurst.model.User;
import com.example.bratwurst.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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

    @GetMapping("/")
    public String login(){
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model){
        User user = userService.getLogin(username, password);

        if (user == null){

            String error = "Username or password was incorrect!";

            System.out.println(error);

            model.addAttribute("error", "logError");

            return "index";

        } else{

            session.setAttribute("login", user);

            return "redirect:/";
        }
    }
}
