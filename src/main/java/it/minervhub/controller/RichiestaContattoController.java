package it.minervhub.controller;

import it.minervhub.model.InviaRichiestaDTO;
import it.minervhub.service.RichiestaContattoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Import corretto
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

        // 1. Definiamo l'URL di ritorno
        // IMPORTANTE: Deve essere "/detail/" perché il tuo AnnuncioController usa quello.
        // Se usi "/dettaglio/", il sito si rompe dopo l'invio.
        String redirectUrl = "redirect:/annuncio/detail/" + richiestaDTO.getIdAnnuncio();

        // Controllo ID nullo
        if (richiestaDTO.getIdAnnuncio() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore tecnico: Annuncio non identificato.");
            return "redirect:/annuncio";
        }

        // Controllo Errori Form (es. messaggio vuoto)
        if (bindingResult.hasErrors()) {
            String errore = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errore);
            return redirectUrl;
        }

        try {
            // Invio effettivo
            richiestaContattoService.inviaRichiesta(principal.getName(), richiestaDTO);

            // Messaggio di successo
            redirectAttributes.addFlashAttribute("successMessage", "Richiesta inviata con successo! Il tutor ti risponderà presto.");

        } catch (IllegalArgumentException e) {
            // Errore previsto (es. hai già inviato richiesta)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            // Errore imprevisto
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Si è verificato un errore imprevisto.");
        }

        return redirectUrl;
    }

    // --- GESTIONE LISTE (MIE RICHIESTE) ---
    @GetMapping("/mie")
    public String visualizzaRichieste(
            @RequestParam(defaultValue = "inviate") String tipo,
            Model model,
            Principal principal) {

        String email = principal.getName();
        model.addAttribute("tipoCorrente", tipo);

        if ("ricevute".equals(tipo)) {
            var richieste = richiestaContattoService.findRichiesteRicevute(email);
            model.addAttribute("richieste", richieste);
            model.addAttribute("titoloPagina", "Richieste Ricevute");
        } else {
            var richieste = richiestaContattoService.findRichiesteInviate(email);
            model.addAttribute("richieste", richieste);
            model.addAttribute("titoloPagina", "Richieste Inviate");
        }

        return "mie-richieste";
    }

    // --- GESTIONE AZIONI (ACCETTA/RIFIUTA) ---
    @PostMapping("/gestisci")
    public String gestisciRichiesta(
            @RequestParam("id") Long idRichiesta,
            @RequestParam("nuovoStato") String nuovoStato,
            @RequestParam(value = "risposta", required = false) String risposta,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            richiestaContattoService.gestisciRichiesta(idRichiesta, principal.getName(), nuovoStato, risposta);
            String msg = nuovoStato.equals("ACCETTATA") ? "Richiesta accettata!" : "Richiesta declinata.";
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Errore: " + e.getMessage());
        }

        return "redirect:/richieste/mie?tipo=ricevute";
    }
}