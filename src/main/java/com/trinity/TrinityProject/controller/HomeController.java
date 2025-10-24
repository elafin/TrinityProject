package com.trinity.TrinityProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(Model model){
        model.addAttribute("home1", "Witaj na stronie domowej!");
        model.addAttribute("home2", "Znajdziesz tu info o stronie.");
        return "home";
    }
}
