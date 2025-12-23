package it.minervhub.controller;

import it.minervhub.exceptions.InvalidFiltroException;
import it.minervhub.model.Annuncio;
import it.minervhub.service.BachecaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller responsabile della gestione della bacheca degli annunci.
 * <p>
 * Fornisce funzionalità di visualizzazione della bacheca pubblica e
 * di dettaglio del singolo annuncio. Gestisce inoltre la validazione
 * dei parametri di filtro e l'interazione con il livello di servizio.
 */
@Controller
public class BachecaController {

    private final BachecaService bachecaService;

    /**
     * Costruttore con injection del servizio di gestione della bacheca.
     *
     * @param bachecaService service che fornisce l'accesso agli annunci
     */
    public BachecaController(BachecaService bachecaService) {
        this.bachecaService = bachecaService;
    }

    /**
     * Visualizza la bacheca degli annunci.
     * <p>
     * Se uno o più parametri di filtro sono presenti, viene effettuata
     * una ricerca filtrata; in caso contrario vengono mostrati tutti
     * gli annunci disponibili. Eventuali errori di validazione sui
     * parametri di input vengono intercettati e comunicati all'utente.
     *
     * @param corsoLaurea corso di laurea su cui filtrare (opzionale)
     * @param esame nome dell'esame su cui filtrare (opzionale)
     * @param tariffaMax tariffa oraria massima consentita (opzionale)
     * @param model modello utilizzato per passare i dati alla vista
     * @return nome della vista Thymeleaf della bacheca
     */
    @GetMapping("/bacheca")
    public String bacheca(
            @RequestParam(required = false) String corsoLaurea,
            @RequestParam(required = false) String esame,
            @RequestParam(required = false) Integer tariffaMax,
            Model model
    ) {

        try {

            if (corsoLaurea != null && corsoLaurea.isEmpty()) corsoLaurea = null;
            if (esame != null && esame.isEmpty()) esame = null;

            List<Annuncio> annunci;

            if (corsoLaurea != null || esame != null || tariffaMax != null) {
                annunci = bachecaService.getAnnunciFiltrati(corsoLaurea, esame, tariffaMax);
            } else {
                annunci = bachecaService.getAnnunciDisponibili();
            }

            model.addAttribute("annunci", annunci);

        } catch (InvalidFiltroException e) {

            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("annunci", bachecaService.getAnnunciDisponibili());
        }

        return "bacheca";
    }

    /**
     * Visualizza il dettaglio di un singolo annuncio.
     *
     * @param id identificativo dell'annuncio
     * @param model modello utilizzato per passare l'annuncio alla vista
     * @return vista di dettaglio dell'annuncio o pagina 404 se non trovato
     */
    @GetMapping("/bacheca/{id}")
    public String visualizzaAnnuncio(
            @PathVariable Long id,
            Model model
    ) {

        Annuncio annuncio = bachecaService.getAnnuncioById(id);

        if (annuncio == null) {
            return "error/404";
        }

        model.addAttribute("annuncio", annuncio);
        return "annuncio";
    }

}
