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

/**
 * Controller Spring MVC responsabile della gestione delle richieste di contatto.
 * <p>
 * Questa classe gestisce il flusso web per:
 * <ul>
 * <li>L'invio di nuove richieste di contatto da parte degli studenti.</li>
 * <li>La visualizzazione delle liste di richieste (inviate e ricevute).</li>
 * <li>La gestione delle richieste da parte del tutor (accettazione/rifiuto).</li>
 * <li>L'annullamento delle richieste pendenti da parte dello studente.</li>
 * </ul>
 */
@Controller
@RequestMapping("/richieste")
public class RichiestaContattoController {

    private final RichiestaContattoService richiestaContattoService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param richiestaContattoService Il service che contiene la logica di business per le richieste.
     */
    public RichiestaContattoController(RichiestaContattoService richiestaContattoService) {
        this.richiestaContattoService = richiestaContattoService;
    }

    /**
     * Gestisce la richiesta POST per inviare una nuova richiesta di contatto a un tutor.
     * <p>
     * Esegue la validazione dei dati in ingresso (inclusa la lunghezza del messaggio).
     * In caso di errori (validazione o logica di business), reindirizza con un messaggio di errore.
     * In caso di successo, crea la richiesta e notifica l'utente.
     *
     * @param richiestaDTO       L'oggetto DTO contenente i dati del form (ID annuncio e messaggio).
     * @param bindingResult      Il risultato della validazione del DTO.
     * @param principal          L'oggetto di sicurezza che rappresenta l'utente loggato (Allievo).
     * @param redirectAttributes Attributi per passare messaggi flash (successo/errore) al redirect.
     * @return La stringa di reindirizzamento alla bacheca o alla pagina precedente.
     */
    @PostMapping("/invia")
    public String inviaRichiesta(
            @ModelAttribute @Valid InviaRichiestaDTO richiestaDTO,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        String redirectUrl = "redirect:/bacheca"; // URL di default dopo l'invio

        // Controllo difensivo: l'ID annuncio deve essere presente
        if (richiestaDTO.getIdAnnuncio() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore tecnico: Annuncio non identificato.");
            return "redirect:/annuncio";
        }

        // Controllo validazione form (es. messaggio troppo lungo)
        if (bindingResult.hasErrors()) {
            String errore = bindingResult.getFieldError().getDefaultMessage();
            redirectAttributes.addFlashAttribute("errorMessage", errore);
            return redirectUrl; // Ritorna alla bacheca con l'errore
        }

        try {
            // Tenta l'invio tramite il service
            richiestaContattoService.inviaRichiesta(principal.getName(), richiestaDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Richiesta inviata con successo! Il tutor ti risponderà presto.");
            return redirectUrl;

        } catch (IllegalArgumentException e) {
            // Gestione errori di business (es. richiesta duplicata, auto-invio)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return redirectUrl;
        } catch (Exception e) {
            // Gestione errori imprevisti
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Si è verificato un errore imprevisto.");
            return redirectUrl;
        }
    }

    /**
     * Visualizza la pagina contenente la lista delle richieste dell'utente loggato.
     * <p>
     * In base al parametro "tipo", mostra le richieste inviate (per gli studenti)
     * o le richieste ricevute (per i tutor).
     *
     * @param tipo      Il tipo di lista da visualizzare: "inviate" (default) o "ricevute".
     * @param model     Il modello per passare i dati alla vista Thymeleaf.
     * @param principal L'utente loggato.
     * @return Il nome della vista Thymeleaf ("mie-richieste").
     */
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

    /**
     * Gestisce l'azione del tutor per accettare o declinare una richiesta ricevuta.
     *
     * @param idRichiesta       L'ID della richiesta da gestire.
     * @param nuovoStato        Il nuovo stato da assegnare ("ACCETTATA" o "DECLINATA").
     * @param risposta          (Opzionale) Un messaggio di risposta al contatto.
     * @param principal         Il tutor loggato che esegue l'azione.
     * @param redirectAttributes Attributi per i messaggi flash.
     * @return Redirect alla lista delle richieste ricevute.
     */
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

    /**
     * Permette a uno studente di annullare una richiesta inviata fintanto che è ancora "IN_ATTESA".
     *
     * @param idRichiesta       L'ID della richiesta da annullare.
     * @param principal         Lo studente loggato che ha creato la richiesta.
     * @param redirectAttributes Attributi per i messaggi flash.
     * @return Redirect alla lista delle richieste inviate.
     */
    @PostMapping("/annulla")
    public String annullaRichiesta(
            @RequestParam("id") Long idRichiesta,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            richiestaContattoService.annullaRichiesta(idRichiesta, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Richiesta annullata correttamente.");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Impossibile annullare: " + e.getMessage());
        }

        // Ritorna alla lista delle richieste INVIATE
        return "redirect:/richieste/mie?tipo=inviate";
    }
}