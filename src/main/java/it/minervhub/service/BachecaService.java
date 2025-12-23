package it.minervhub.service;

import it.minervhub.exceptions.InvalidFiltroException;
import it.minervhub.model.Annuncio;
import it.minervhub.repository.AnnuncioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BachecaService {

    private final AnnuncioRepository annuncioRepository;

    public BachecaService(AnnuncioRepository annuncioRepository) {
        this.annuncioRepository = annuncioRepository;
    }

    public List<Annuncio> getAnnunciDisponibili() {
        return annuncioRepository.findByDisponibileTrue();
    }

    public List<Annuncio> getAnnunciFiltrati(String corsoLaurea, String esame, Integer tariffaMax) {



        if (corsoLaurea != null && corsoLaurea.length() > 50) {
            throw new InvalidFiltroException("Il corso di laurea non può superare i 50 caratteri.");
        }

        if (esame != null && esame.length() > 50) {
            throw new InvalidFiltroException("Il nome dell’esame non può superare i 50 caratteri.");
        }

        if (tariffaMax != null && (tariffaMax < 5 || tariffaMax > 50)) {
            throw new InvalidFiltroException("La tariffa oraria deve essere compresa tra 5 e 50 euro.");
        }

        return annuncioRepository.filtraAnnunci(corsoLaurea, esame, tariffaMax);
    }
    public Annuncio getAnnuncioById(Long id){
        Optional<Annuncio> annuncio = annuncioRepository.findById(id);
        return annuncio.orElse(null);
    }
}
