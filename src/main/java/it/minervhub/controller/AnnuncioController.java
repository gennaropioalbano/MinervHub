package it.minervhub.controller;

import it.minervhub.model.Annuncio;
import it.minervhub.model.AnnuncioDto;
import it.minervhub.model.Utente;
import it.minervhub.service.AnnuncioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/annuncio")
public class AnnuncioController {

    @Autowired
    private AnnuncioService annuncioService;

    @GetMapping("/annuncio/{id}")
    public String annuncio(@PathVariable Long id, Model model) {

        Annuncio annuncio = annuncioService.getAnnuncioById(id);

        if (annuncio == null) {
            return "redirect:/annuncio";
        }

        model.addAttribute("annuncio", annuncio);
        return "annuncio";
    }

    // --- CREAZIONE ---
    @GetMapping("/creaAnnuncio")
    public String showCreatePage(Model model) {
        model.addAttribute("annuncioDto", new AnnuncioDto());
        return "/creaAnnuncio";
    }

    @PostMapping("/creaAnnuncio")
    public String creaAnnuncio(
            @Valid @ModelAttribute("annuncioDto") AnnuncioDto dto,
            BindingResult bindingResult,
            Principal principal) {

        if (bindingResult.hasErrors()) {
            return "/creaAnnuncio";
        }

        annuncioService.save(dto, principal.getName());

        return "redirect:/annuncio/miei";
    }

    // --- 5. MODIFICA ---
    @GetMapping("/modificaAnnuncio/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        Optional<Annuncio> annuncioOpt = Optional.ofNullable(annuncioService.getAnnuncioById(id));

        if (annuncioOpt.isEmpty()) {
            return "redirect:/annuncio/miei";
        }

        AnnuncioDto dto = annuncioService.mapEntityToDto(annuncioOpt.get());
        model.addAttribute("annuncioDto", dto);
        model.addAttribute("annuncioId", id);

        return "modificaAnnuncio";
    }

    @PostMapping("/eliminaAnnuncio/{id}")
    public String editAnnuncio(
            @PathVariable Long id,
            @Valid @ModelAttribute("annuncioDto") AnnuncioDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("annuncioId", id);
            return "modificaAnnuncio";
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
        annuncioService.eliminaAnnuncio(id);
        return "redirect:/annuncio/miei";
    }

    // --- I MIEI ANNUNCI ---
    @GetMapping("/miei")
    public String showMyAnnunci(Model model, Principal principal) {
        model.addAttribute("annunci", annuncioService.findByAutoreEmail(principal.getName()));

        return "mieiAnnunci";
    }

    @GetMapping("/{id}")
    public String showAnnuncioDetail(@PathVariable Long id, Model model) {

        Optional<Annuncio> annuncioOpt = Optional.ofNullable(annuncioService.getAnnuncioById(id));

        if (annuncioOpt.isEmpty()) {
            return "error/404";
        }

        Annuncio annuncio = annuncioOpt.get();
        model.addAttribute("annuncio", annuncio);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUsername", auth.getName());

        return "mioAnnuncio"; // UNICA vista di dettaglio
    }
}