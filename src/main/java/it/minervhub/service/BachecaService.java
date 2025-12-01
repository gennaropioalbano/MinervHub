package it.minervhub.service;

import it.minervhub.repository.AnnuncioRepository;
import org.springframework.stereotype.Service;

@Service
public class BachecaService {
    private AnnuncioRepository annuncioRepository;

    public BachecaService(AnnuncioRepository annuncioRepository) {
        this.annuncioRepository = annuncioRepository;
    }


}
