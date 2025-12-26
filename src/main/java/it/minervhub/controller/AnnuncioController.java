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

/**
 * Controller che gestisce le operazioni relative agli annunci.
 * Fornisce le funzionalità di visualizzazione, creazione, modifica
 * ed eliminazione degli annunci.
 */
@Controller
@RequestMapping("/annuncio")
public class AnnuncioController {

    /** Service per la gestione della logica di business degli annunci */
    @Autowired
    private AnnuncioService annuncioService;

    /**
     * Mostra la pagina di dettaglio di un annuncio specifico.
     *
     * @param id identificativo dell'annuncio
     * @param model model utilizzato per passare i dati alla view
     * @return la view dell'annuncio oppure redirect se non trovato
     */
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

    /**
     * Mostra il form per la creazione di un nuovo annuncio.
     *
     * @param model model utilizzato per passare il DTO alla view
     * @return la view del form di creazione annuncio
     */
    @GetMapping("/creaAnnuncio")
    public String mostraFormAnnuncio(Model model) {

        model.addAttribute("annuncioDto", new AnnuncioDto());

        return "/creaAnnuncio";
    }

    /**
     * Gestisce la creazione di un nuovo annuncio.
     *
     * @param dto DTO contenente i dati dell'annuncio
     * @param bindingResult risultato della validazione
     * @param principal utente autenticato
     * @return redirect alla lista degli annunci dell'utente
     */
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

    /**
     * Mostra la pagina di modifica di un annuncio esistente.
     *
     * @param id identificativo dell'annuncio
     * @param model model utilizzato per passare i dati alla view
     * @return la view di modifica annuncio
     */
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

    /**
     * Gestisce la modifica di un annuncio esistente.
     *
     * @param id identificativo dell'annuncio
     * @param dto DTO contenente i dati aggiornati
     * @param bindingResult risultato della validazione
     * @param principal utente autenticato
     * @param model model utilizzato per la view
     * @return redirect alla pagina di dettaglio dell'annuncio
     */
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

    /**
     * Elimina un annuncio esistente.
     *
     * @param id identificativo dell'annuncio
     * @return redirect alla lista degli annunci dell'utente
     */
    @GetMapping("/delete/{id}")
    public String deleteAnnuncio(@PathVariable Long id) {
        annuncioService.eliminaAnnuncio(id);
        return "redirect:/annuncio/miei";
    }

    /**
     * Mostra la lista degli annunci creati dall'utente autenticato.
     *
     * @param model model utilizzato per passare la lista alla view
     * @param principal utente autenticato
     * @return la view con gli annunci dell'utente
     */
    @GetMapping("/miei")
    public String showMyAnnunci(Model model, Principal principal) {

        model.addAttribute("annunci", annuncioService.findByAutoreEmail(principal.getName()));

        return "mieiAnnunci";
    }
}