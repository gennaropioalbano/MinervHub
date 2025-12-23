package it.minervhub.controller;

import it.minervhub.model.Annuncio;
import it.minervhub.model.AnnuncioDto;
import it.minervhub.service.AnnuncioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/annunci")
public class AnnuncioController {

    @Autowired
    private AnnuncioService annuncioService;

    // --- 1. BACHECA (LISTA DI TUTTI GLI ANNUNCI) ---
    // Risolve l'errore "No static resource annunci"
    @GetMapping({"", "/"})
    public String showAnnuncioList(Model model) {
        model.addAttribute("annunci", annuncioService.findAll());
        return "bacheca"; // Assicurati che il file si chiami bacheca.html
    }

    // --- 2. DETTAGLIO ANNUNCIO ---
    // Mappato su /annunci/{id} (es. /annunci/4)
    @GetMapping("/{id}")
    public String showAnnuncioDetail(@PathVariable Long id, Model model) {
        Optional<Annuncio> annuncioOpt = annuncioService.findById(id);

        if (annuncioOpt.isEmpty()) {
            return "redirect:/annunci"; // Se non esiste, torna in bacheca
        }

        model.addAttribute("annuncio", annuncioOpt.get());

        // Passiamo l'utente corrente (serve per mostrare/nascondere i bottoni modifica/elimina)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            model.addAttribute("currentUsername", auth.getName());
        }

        // IMPORTANTE: Questo deve coincidere con il nome del tuo file HTML di dettaglio.
        // Se il file si chiama "annuncio.html", lascia "annuncio".
        // Se si chiama "mioAnnuncio.html", scrivi "mioAnnuncio".
        return "annuncio";
    }

    // --- 3. I MIEI ANNUNCI ---
    @GetMapping("/miei")
    public String showMyAnnunci(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Filtra solo gli annunci dell'utente loggato
        model.addAttribute("annunci", annuncioService.findByAutoreEmail(email));

        return "mieiAnnunci"; // Assicurati che il file si chiami mieiAnnunci.html
    }

    // --- 4. CREAZIONE ---
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("annuncioDto", new AnnuncioDto());
        return "createAnnuncio";
    }

    @PostMapping("/create")
    public String createAnnuncio(
            @Valid @ModelAttribute("annuncioDto") AnnuncioDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "createAnnuncio";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        annuncioService.createAnnuncio(dto, auth.getName());

        return "redirect:/annunci";
    }

    // --- 5. MODIFICA ---
    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        Optional<Annuncio> annuncioOpt = annuncioService.findById(id);

        if (annuncioOpt.isEmpty()) {
            return "redirect:/annunci";
        }

        AnnuncioDto dto = annuncioService.mapEntityToDto(annuncioOpt.get());
        model.addAttribute("annuncioDto", dto);
        model.addAttribute("annuncioId", id);

        return "editAnnuncio";
    }

    @PostMapping("/edit/{id}")
    public String editAnnuncio(
            @PathVariable Long id,
            @Valid @ModelAttribute("annuncioDto") AnnuncioDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("annuncioId", id);
            return "editAnnuncio";
        }

        boolean successo = annuncioService.updateAnnuncio(id, dto);

        if (!successo) {
            return "redirect:/annunci";
        }

        return "redirect:/annunci";
    }

    // --- 6. ELIMINAZIONE ---
    @GetMapping("/delete/{id}")
    public String deleteAnnuncio(@PathVariable Long id) {
        annuncioService.deleteAnnuncio(id);
        return "redirect:/annunci";
    }
}