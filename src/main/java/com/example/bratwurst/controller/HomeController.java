package com.example.bratwurst.controller;

import com.example.bratwurst.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.logging.Logger;

@Controller
public class HomeController {

    public HomeController() {
    }

    Logger log = Logger.getLogger(HomeController.class.getName());

    @Autowired
    HomeService homeService;

    @GetMapping()
    public String index(Model model){
        log.info("index called");

        return "index";
    }
}
