package it.minervhub.controller;

import it.minervhub.service.AnnuncioService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnnuncioController {

    private AnnuncioService annuncioService;

    public AnnuncioController(AnnuncioService annuncioService) {
        this.annuncioService = annuncioService;
    }
}
