package it.minervhub.controller;

import it.minervhub.repository.RichiestaContattoRepository;
import it.minervhub.service.RichiestaContattoService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RichiestaContattoController {

    private final RichiestaContattoService richiestaContattoService;

    public RichiestaContattoController(RichiestaContattoService richiestaContattoService) {
        this.richiestaContattoService = richiestaContattoService;
    }
}
