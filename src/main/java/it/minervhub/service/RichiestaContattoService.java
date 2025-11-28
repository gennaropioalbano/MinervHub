package it.minervhub.service;

import it.minervhub.repository.RichiestaContattoRepository;
import org.springframework.stereotype.Service;

@Service
public class RichiestaContattoService {

    private final RichiestaContattoRepository richiestaContattoRepository;

    public RichiestaContattoService(RichiestaContattoRepository richiestaContattoRepository) {
        this.richiestaContattoRepository = richiestaContattoRepository;
    }
}
