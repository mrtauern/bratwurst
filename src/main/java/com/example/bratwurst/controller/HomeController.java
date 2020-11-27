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

    @GetMapping("/home")
    public String home(HttpSession session, Model model){

        if (session.getAttribute("login") != null){
            model.addAttribute("users", userService.getUsers());
            return "home";
        }else {

            model.addAttribute("notLoggedIn", "notLoggedIn");
            return "index";
        }

    }

    @GetMapping("/messages")
    public String messages(){

        return "messages";
    }

    @GetMapping("/notifications")
    public String notifications() {

        return "notifications";
    }


    // LOGIN
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

            return "redirect:/";

        } else{

            session.setAttribute("login", user);

            return "redirect:/home";
        }
    }

    // LOGOUT

    @GetMapping("/logout")
    public String logout(HttpSession session){

        log.info("logout called... ");

        session.removeAttribute("login");

        log.info("session terminated");

        return "index";
    }

    // SIGN UP

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, @RequestParam String psw_repeat, HttpSession session, Model model){

       boolean strongPassword = userService.passwordStrong(user.getPassword());

       User theUser = userService.addUser(user, psw_repeat);

       if (strongPassword != true) {
           model.addAttribute("password_not_strong", "true");
           System.out.println("password is not strong");
       }else if (theUser == null){
           model.addAttribute("password_different_error", "true");
           System.out.println("passwords are different");
       }else if (theUser.getUsername() == null){
           model.addAttribute("email_in_use_error", "true");
           System.out.println("email is already registered");
       }else if (theUser.getEmail() == null){
           model.addAttribute("username_taken_error", "true");
           System.out.println("Username already taken");
       }else {
           session.setAttribute("login", user);
           return "redirect:/home";
       }
           return "signup";
    }

}
