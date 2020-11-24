package com.example.bratwurst.controller;

import com.example.bratwurst.ViewModel.UserViewModel;
import com.example.bratwurst.model.User;
import com.example.bratwurst.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@Controller
public class UserController {

    Logger log = Logger.getLogger(UserController.class.getName());
    UserViewModel currentUser = new UserViewModel();

    //RETURN STRINGS
    private final String REDIRECT = "redirect:/";
    private final String HOME = "home";
    private final String LOGIN = "login";
    private final String SIGNUP = "signup";

    @Autowired
    UserService userService;

    //HOME
    @GetMapping("/home")
    public String home(Model model){
        log.info("--- getUser() called ---");

        if(currentUser == null || currentUser.getUsername() == null){
            return REDIRECT + LOGIN;
        }

        model.addAttribute("users", currentUser);

        return HOME;
    }

    @GetMapping("/login")
    public String login(Model model) {
        log.info("--- Login() called ---");
        currentUser = null;
        model.addAttribute("users", new UserViewModel());
        //model.addAttribute("pageTitle", "Login");
        model.addAttribute("isLogin", true);

        return LOGIN;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserViewModel uvm, Model model, RedirectAttributes redirAttr) {

        if (uvm.getUsername() != "" && uvm.getPassword() != "") {

            UserViewModel user = userService.login(uvm.getUsername(), uvm.getPassword());

            if (user != null) {
                if(user.getPassword() != null && user.getUsername() != null) {
                    redirAttr.addFlashAttribute("loginsuccess", true);
                    redirAttr.addFlashAttribute("username", user.getUsername());

                    currentUser = user;

                    return REDIRECT + HOME;
                }

            } else {

                redirAttr.addFlashAttribute("loginError", true);

                return REDIRECT + LOGIN;
            }
        } else {
            redirAttr.addFlashAttribute("loginError", true);
            return REDIRECT + LOGIN;
        }

        redirAttr.addFlashAttribute("loginError", true);
        return REDIRECT + LOGIN;
    }

    @GetMapping("/signup")
    public String signup(Model model){

        model.addAttribute("user", new UserViewModel());

        return SIGNUP;
    }

    @PostMapping("/signup")
    public String signup (@ModelAttribute UserViewModel uvm, Model model){

        userService.insertUser(uvm);

        return REDIRECT;
    }
}
