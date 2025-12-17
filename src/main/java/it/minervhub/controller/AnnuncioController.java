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

    // UNICA DIPENDENZA: Il Service (Niente Repository qui!)
    @Autowired
    private AnnuncioService annuncioService;

    // --- LISTA BACHECA ---
    @GetMapping({"", "/"})
    public String showAnnuncioList(Model model) {
        model.addAttribute("annunci", annuncioService.findAll());
        return "bacheca";
    }

    // --- DETTAGLIO ANNUNCIO ---
    @GetMapping("/detail/{id}")
    public String showAnnuncioDetail(@PathVariable Long id, Model model) {
        Optional<Annuncio> annuncioOpt = annuncioService.findById(id);

        if (annuncioOpt.isEmpty()) {
            return "redirect:/annunci";
        }

        model.addAttribute("annuncio", annuncioOpt.get());

        // Passiamo l'utente corrente per gestire i bottoni "Modifica/Elimina" nella vista
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("currentUsername", currentUsername);

        return "annuncio";
    }

    // --- CREAZIONE ---
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

        // Recupero chi è loggato
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Delega tutta la logica al service
        annuncioService.createAnnuncio(dto, email);

        return "redirect:/annunci";
    }

    // --- MODIFICA ---
    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        Optional<Annuncio> annuncioOpt = annuncioService.findById(id);

        if (annuncioOpt.isEmpty()) {
            return "redirect:/annunci";
        }

        // Usiamo il metodo del service per convertire l'Entità in DTO (per riempire i campi del form)
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

        // Il service prova ad aggiornare. Se torna false (ID non trovato), redirect.
        boolean successo = annuncioService.updateAnnuncio(id, dto);

        if (!successo) {
            return "redirect:/annunci";
        }

        return "redirect:/annunci";
    }

    // --- ELIMINAZIONE ---
    @GetMapping("/delete/{id}")
    public String deleteAnnuncio(@PathVariable Long id) {
        annuncioService.deleteAnnuncio(id);
        return "redirect:/annunci";
    }

    // --- I MIEI ANNUNCI ---
    @GetMapping("/miei")
    public String showMyAnnunci(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Il service filtra per email dell'autore
        model.addAttribute("annunci", annuncioService.findByAutoreEmail(email));

        return "mieiAnnunci"; // Assicurati di avere questo template o usa "bacheca" se condividono la vista
    }
}