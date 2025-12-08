package it.minervhub.controller;

import it.minervhub.model.Annuncio;
import it.minervhub.service.BachecaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        System.out.println("✅ Accesso pubblico alla bacheca");
        List<Annuncio> annunci;

        if (corsoLaurea != null || esame != null || tariffaMax != null) {
            annunci = bachecaService.getAnnunciFiltrati(corsoLaurea, esame, tariffaMax);
        } else {
            annunci = bachecaService.getAnnunciDisponibili();
        }

        model.addAttribute("annunci", annunci);
        return "bacheca"; // caricherà bacheca.html
    }
}
