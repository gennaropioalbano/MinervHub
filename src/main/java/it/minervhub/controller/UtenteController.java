package it.minervhub.controller;

import it.minervhub.model.Utente;
import it.minervhub.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    // 1. Pagina di Login
    @GetMapping("/login")
    public String login() {
        return "login"; // Cerca login.html
    }

    // 2. Mostra il form di Registrazione
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("utente", new Utente());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute Utente utente) {
        utenteService.salvaUtente(utente);
        return "redirect:/login?success";
    }
}
