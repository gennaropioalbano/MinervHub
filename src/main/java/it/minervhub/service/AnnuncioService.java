package it.minervhub.service;

import it.minervhub.repository.AnnuncioRepository;
import org.springframework.stereotype.Service;

@Service
public class AnnuncioService {

    private AnnuncioRepository annuncioRepository;

    public AnnuncioService(AnnuncioRepository annuncioRepository) {
        this.annuncioRepository = annuncioRepository;
    }
}