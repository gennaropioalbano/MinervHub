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
import java.util.Optional;

@Controller
@RequestMapping("/annuncio")
public class AnnuncioController {

    @Autowired
    private AnnuncioService annuncioService;

    @GetMapping("/{id}")
    public String mostraAnnuncio(@PathVariable Long id, Model model) {

        Annuncio annuncio = annuncioService.getAnnuncioById(id);

        if (annuncio == null) {
            return "redirect:/annuncio";
        }

        model.addAttribute("annuncio", annuncio);

        model.addAttribute("mostraPublisher", false);

        return "annuncio";
    }

    @GetMapping("/creaAnnuncio")
    public String mostraFormAnnuncio(Model model) {

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

        annuncioService.modificaAnnuncio(dto, principal.getName());

        return "redirect:/annuncio/miei";
    }

    @GetMapping("/modificaAnnuncio/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        Optional<Annuncio> annuncioOpt = Optional.ofNullable(annuncioService.getAnnuncioById(id));

        if (annuncioOpt.isEmpty()) {
            return "/modificaAnnuncio";
        }

        AnnuncioDto dto = annuncioService.mapEntityToDto(annuncioOpt.get());
        model.addAttribute("annuncioDto", dto);
        model.addAttribute("annuncioId", id);

        return "modificaAnnuncio";
    }

    @PostMapping("/modificaAnnuncio/{id}")
    public String modificaAnnuncio(
            @PathVariable Long id,
            @Valid @ModelAttribute("annuncioDto") AnnuncioDto dto,
            BindingResult bindingResult,
            Principal principal, Model model) {

        if (bindingResult.hasErrors()) {
            return "/modificaAnnuncio";
        }

        annuncioService.modificaAnnuncio(id, dto, principal.getName());

        model.addAttribute("mostraPublisher", false);

        return "redirect:/annuncio/{id}";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnnuncio(@PathVariable Long id) {
        annuncioService.eliminaAnnuncio(id);
        return "redirect:/annuncio/miei";
    }

    @GetMapping("/miei")
    public String showMyAnnunci(Model model, Principal principal) {

        model.addAttribute("annunci", annuncioService.findByAutoreEmail(principal.getName()));

        return "mieiAnnunci";
    }
}