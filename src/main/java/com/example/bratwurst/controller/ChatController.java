package com.example.bratwurst.controller;

import com.example.bratwurst.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Controller
public class ChatController
{
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
}
