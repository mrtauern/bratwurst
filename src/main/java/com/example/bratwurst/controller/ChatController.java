package com.example.bratwurst.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ChatController
{
    @GetMapping("/chat/{receiver}")
    public String getChatUI(@PathVariable int receiver, Model model)
    {
        model.addAttribute("receiver", receiver);
        model.addAttribute("sender", 1);
        return "chat";
    }
}
