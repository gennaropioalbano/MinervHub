package it.minervhub.controller;

import it.minervhub.repository.RichiestaContattoRepository;
import it.minervhub.service.RichiestaContattoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RichiestaContattoController {

    private final RichiestaContattoService richiestaContattoService;

    public RichiestaContattoController(RichiestaContattoService richiestaContattoService) {
        this.richiestaContattoService = richiestaContattoService;
    }

    @GetMapping("/inviaRichiesta")
    public String invioRichiestaContatto(
            @RequestParam("annuncioId") Long annuncioId,
            Model model
    ) {
        // TODO
        return "inviaRichiesta";
    }

}
