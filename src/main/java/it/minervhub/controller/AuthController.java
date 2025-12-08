package it.minervhub.controller;

import it.minervhub.model.Utente;
import it.minervhub.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("utente", new Utente());
        return "register";
    }

    @PostMapping("/register")
    public String registerUtente(@ModelAttribute Utente utente) {
        utenteService.save(utente);
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Utente utente = utenteService.findByEmail(userDetails.getUsername());
        model.addAttribute("name", utente.getNome());
        model.addAttribute("email", utente.getEmail());
        return "home";
    }
}