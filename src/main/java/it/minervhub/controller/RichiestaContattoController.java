package it.minervhub.controller;

import it.minervhub.model.InviaRichiestaDTO;
import it.minervhub.service.RichiestaContattoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // ... (Il metodo @PostMapping("/invia") rimane uguale a prima) ...

    /**
     * Gestisce la visualizzazione delle richieste (Inviate o Ricevute)
     * Url esempi:
     * /richieste/mie?tipo=inviate (Default)
     * /richieste/mie?tipo=ricevute
     */
    @GetMapping("/mie")
    public String visualizzaRichieste(
            @RequestParam(defaultValue = "inviate") String tipo,
            Model model,
            Principal principal) {

        String email = principal.getName();
        model.addAttribute("tipoCorrente", tipo); // Serve all'HTML per colorare il bottone attivo

        if ("ricevute".equals(tipo)) {
            // Carico le richieste che ho RICEVUTO (io sono il Tutor)
            var richieste = richiestaContattoService.findRichiesteRicevute(email);
            model.addAttribute("richieste", richieste);
            model.addAttribute("titoloPagina", "Richieste Ricevute");
        } else {
            // Default: Carico le richieste che ho INVIATO (io sono l'Allievo)
            var richieste = richiestaContattoService.findRichiesteInviate(email);
            model.addAttribute("richieste", richieste);
            model.addAttribute("titoloPagina", "Richieste Inviate");
        }

        return "mie-richieste"; // Il nome del file HTML aggiornato
    }

    // ... altri metodi ...

    @PostMapping("/gestisci")
    public String gestisciRichiesta(
            @RequestParam("id") Long idRichiesta,
            @RequestParam("nuovoStato") String nuovoStato,
            @RequestParam(value = "risposta", required = false) String risposta,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            // Chiama il service passando l'email di chi è loggato (per sicurezza)
            richiestaContattoService.gestisciRichiesta(idRichiesta, principal.getName(), nuovoStato, risposta);

            String msg = nuovoStato.equals("ACCETTATA") ? "Richiesta accettata!" : "Richiesta declinata.";
            redirectAttributes.addFlashAttribute("successMessage", msg);

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Errore: " + e.getMessage());
        }

        // Ricarica la pagina delle richieste RICEVUTE
        return "redirect:/richieste/mie?tipo=ricevute";
    }
}