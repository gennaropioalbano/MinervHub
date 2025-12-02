package it.minervhub.service;

import it.minervhub.model.Annuncio;
import it.minervhub.repository.AnnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnuncioService {

    @Autowired
    private AnnuncioRepository annuncioRepository;

    public AnnuncioService(AnnuncioRepository annuncioRepository) {
        this.annuncioRepository = annuncioRepository;
    }

    public List<Annuncio> getAnnunciDisponibili(){
        return annuncioRepository.findByDisponibileTrue();
    }

    public Annuncio getAnnuncioById(Long id){
        Optional<Annuncio> annuncio = annuncioRepository.findById(id);
        return annuncio.orElse(null);
    }

    public void addAnnucio(Annuncio annuncio){
        annuncioRepository.save(annuncio);
    }

    public void updateAnnuncio(Long id, Annuncio annuncio){
        annuncioRepository.save(annuncio);
    }

    public void disabilitaAnnuncio(Long id){
        Annuncio annuncio = annuncioRepository.findById(id).get();
        annuncio.setDisponibile(false);
    }
}