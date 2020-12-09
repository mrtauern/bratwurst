package com.example.bratwurst.controller;

import com.example.bratwurst.model.User;
import com.example.bratwurst.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class ChatController
{

    @Autowired
    UserService userService;

    @CrossOrigin()
    @GetMapping("/chat/{receiver}")
    public String getChatUI(@PathVariable int receiver, HttpSession session, Model model)
    {
        try
        {
            User user = (User)session.getAttribute("login");

            if(receiver == user.getId())
            {
                return "error";
            }

            model.addAttribute("receiver", receiver);
            model.addAttribute("sender", user.getId());
            return "chat";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }

    }

    @CrossOrigin()
    @GetMapping("/friendRequest/{receiver}")
    public String sendFriendRequest(@PathVariable int receiver, HttpSession session, Model model){

        try
        {
            User user = (User)session.getAttribute("login");

            if(receiver == user.getId())
            {
                return "error";
            }

            userService.friendRequest(user.getId(), receiver);

            return "redirect:/home";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    @CrossOrigin()
    @GetMapping("/acceptFriendRequest/{receiver}")
    public String acceptFriendRequest(@PathVariable int receiver, HttpSession session, Model model) {

        try {
            User user = (User) session.getAttribute("login");

            if (receiver == user.getId()) {
                return "error";
            }

            userService.acceptRequest(receiver, user.getId());

            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
}
