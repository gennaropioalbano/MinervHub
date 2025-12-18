package it.minervhub.controller;

import it.minervhub.model.InviaRichiestaDTO;
import it.minervhub.service.RichiestaContattoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/richieste")
public class RichiestaContattoController {

    private final RichiestaContattoService richiestaContattoService;

    public RichiestaContattoController(RichiestaContattoService richiestaContattoService) {
        this.richiestaContattoService = richiestaContattoService;
    }

    @PostMapping("/invia")
    public String inviaRichiesta(
            @ModelAttribute @Valid InviaRichiestaDTO richiestaDTO,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        // --- MODIFICA QUI: Invece di tornare al dettaglio, torniamo alla Bacheca ---
        // Assumo che la tua bacheca sia mappata su "/annunci" o "/home".
        // Se la tua home page è "/", scrivi "redirect:/"
        String redirectUrl = "redirect:/annunci";
        // --------------------------------------------------------------------------

        // Controllo ID nullo (Sicurezza)
        if (richiestaDTO.getIdAnnuncio() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore tecnico: Annuncio non identificato.");
            return redirectUrl;
        }

        // Controllo errori di validazione (es. messaggio vuoto)
        if (bindingResult.hasErrors()) {
            String errore = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errore);
            return redirectUrl;
        }

        try {
            // Tentativo di invio
            richiestaContattoService.inviaRichiesta(principal.getName(), richiestaDTO);

            // Successo!
            redirectAttributes.addFlashAttribute("successMessage", "Richiesta inviata con successo! Il tutor ti risponderà presto.");

        } catch (IllegalArgumentException e) {
            // Errori previsti (es. "Hai già una richiesta in attesa")
            // Questo messaggio verrà mostrato nella bacheca
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Errori imprevisti
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Si è verificato un errore imprevisto.");
        }

        return redirectUrl;
    }
}