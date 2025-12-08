package it.minervhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Gestisce la root (localhost:8080) -> ti manda alla home
    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    // Gestisce la home dopo il login
    @GetMapping("/home")
    public String home() {
        return "home"; // Cerca home.html
    }
}
