package com.example.bratwurst.controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import org.json.JSONException;
import org.springframework.context.annotation.Bean;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.example.bratwurst.model.User;
import com.example.bratwurst.service.SanitizingService;
import com.example.bratwurst.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

@Controller
// @EnableAutoConfiguration
public class HomeController {

    public HomeController() {
    }

    Logger log = Logger.getLogger(HomeController.class.getName());

    @Autowired
    UserService userService;

    private String email;
    private User user;

    @Autowired
    SanitizingService sanitizingService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model){

        if (session.getAttribute("login") != null){
            User u = (User)session.getAttribute("login");

            System.out.println(u);

            model.addAttribute("user", userService.getUserById(u.getId()));

            model.addAttribute("users", userService.getUsers(u.getId()));
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

    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) throws JSONException {

        User user = userService.getLogin(username, password);

        if (user == null){

            String error = "Username or password was incorrect!";

            System.out.println(error);

            model.addAttribute("error", "logError");

            return "redirect:/";

        } else{

            userService.publishMessage(user.getEmail());

            this.user = user;

            return "two_factor_auth";
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
    public String signup(@ModelAttribute User user, @RequestParam String psw_repeat, HttpSession session, Model model) throws JSONException {

       boolean strongPassword = userService.passwordStrong(user.getPassword());

       User theUser = userService.addUser(user, psw_repeat);

        if(theUser != null)
        {
            theUser.setUsername(sanitizingService.sanitizeString(theUser.getUsername()));
            theUser.setCity(sanitizingService.sanitizeString(theUser.getCity()));
            theUser.setCountry(sanitizingService.sanitizeString(theUser.getCountry()));
            theUser.setEmail(sanitizingService.sanitizeString(theUser.getEmail()));
            theUser.setFirst_name(sanitizingService.sanitizeString(theUser.getFirst_name()));
            theUser.setLast_name(sanitizingService.sanitizeString(theUser.getLast_name()));

        }

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
           userService.subscribeToTopic(user.getEmail());
           this.email = user.getEmail();
           // userService.setPolicyFilter(user.getEmail());
           return "welcome";
       }
           return "signup";
       }


    @GetMapping("/setPolicy")
    public String setPolicy() throws JSONException {
        userService.setPolicyFilter(this.email);
        return "index";
    }

    @PostMapping("/Auth_code")
    public String sendSNS(@RequestParam int code, HttpSession session) throws JSONException {
        boolean isValid = userService.validateAuthCode(code);

        if (isValid){
            session.setAttribute("login", this.user);
            return "redirect:/home";
        }else {

            return "two_factor_auth";
        }
    }
}
