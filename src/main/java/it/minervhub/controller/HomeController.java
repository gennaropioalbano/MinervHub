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
public class HomeController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("utente", new Utente());
        return "register";
    }

    @PostMapping("/createUser")
    public String createUser(@ModelAttribute Utente utente, Model model) {
        try {
            System.out.println("=== INIZIO REGISTRAZIONE ===");
            System.out.println("Controller - Ricevuto utente: " + utente);
            System.out.println("Email: " + utente.getEmail());
            System.out.println("Nome: " + utente.getNome());

            utenteService.createUser(utente);

            System.out.println("=== REGISTRAZIONE COMPLETATA ===");
            return "redirect:/login?success";
        } catch (Exception e) {
            System.out.println("=== ERRORE REGISTRAZIONE ===");
            e.printStackTrace();
            model.addAttribute("error", "Errore durante la registrazione: " + e.getMessage());
            return "register";
        }
    }

    // TEST TEMPORANEO
    @GetMapping("/test-controller")
    public String testController() {
        System.out.println("=== CONTROLLER FUNZIONA! ===");
        return "register";
    }
}