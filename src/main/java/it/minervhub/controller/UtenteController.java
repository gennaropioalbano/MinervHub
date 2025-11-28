package it.minervhub.controller;

import it.minervhub.service.UtenteService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }
}
