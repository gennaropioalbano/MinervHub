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

@Controller
public class BachecaController {

    private final BachecaService bachecaService;

    public BachecaController(BachecaService bachecaService) {
        this.bachecaService = bachecaService;
    }

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



    @GetMapping("/bacheca/{id}")
    public String visualizzaAnnuncio(
            @PathVariable Long id, // otteniamo l'ID dall'URL
            Model model
    ) {
        System.out.println("Visualizzazione annuncio con ID: " + id);

        // Recupera l'annuncio dal servizio
        Annuncio annuncio = bachecaService.getAnnuncioById(id);

        // Se l'annuncio non esiste, ritorna un errore 404 (o gestisci il caso in altro modo)
        if (annuncio == null) {
            return "error/404"; // Imposta una pagina di errore personalizzata
        }

        model.addAttribute("annuncio", annuncio);
        return "annuncio"; // carica la pagina di dettaglio annuncio (annuncio.html)
    }

}
