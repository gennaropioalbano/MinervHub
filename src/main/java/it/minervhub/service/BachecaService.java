package it.minervhub.service;

import it.minervhub.model.Annuncio;
import it.minervhub.repository.AnnuncioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BachecaService {
    private AnnuncioRepository annuncioRepository;

    public BachecaService(AnnuncioRepository annuncioRepository) {
        this.annuncioRepository = annuncioRepository;
    }

    public List<Annuncio> getAnnunciDisponibili(){
        return annuncioRepository.findByDisponibileTrue();
    }

    public List<Annuncio> getAnnunciFiltrati(String corsoLaurea, String esame, Integer tariffaMax) {
        return annuncioRepository.filtraAnnunci(corsoLaurea, esame, tariffaMax);
    }

    public Annuncio getAnnuncioById(Long id){
        Optional<Annuncio> annuncio = annuncioRepository.findById(id);
        return annuncio.orElse(null);
    }
}
