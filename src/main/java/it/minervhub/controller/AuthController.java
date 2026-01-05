package it.minervhub.controller;

import it.minervhub.model.Utente;
import it.minervhub.service.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, HttpSession session) {
        // Recuperiamo l'URL della pagina precedente
        String referer = request.getHeader("Referer");

        // Evitiamo di salvare il referer se è nullo, se è la pagina di registrazione o se è il login stesso
        if (referer != null && !referer.contains("/login") && !referer.contains("/register")) {
            session.setAttribute("URL_PRECEDENTE", referer);
        }
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
}