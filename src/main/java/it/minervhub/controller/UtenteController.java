package it.minervhub.controller;

import it.minervhub.model.Utente;
import it.minervhub.service.UtenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utenti")
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping
    public ResponseEntity<Utente> creaUtente(@RequestBody Utente utente) {
        Utente nuovoUtente = utenteService.salvaUtente(utente);
        return new ResponseEntity<>(nuovoUtente, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Utente>> getAllUtente() {
        List<Utente> utenti = utenteService.trovaTuttiGliUtenti();
        return ResponseEntity.ok(utenti);
    }
}
