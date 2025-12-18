package it.minervhub.service;

import it.minervhub.model.InviaRichiestaDTO; // <--- IMPORT CORRETTO
import it.minervhub.model.Annuncio;
import it.minervhub.model.RichiestaContatto;
import it.minervhub.model.StatoRichiesta;
import it.minervhub.model.Utente;
import it.minervhub.repository.RichiestaContattoRepository;
import it.minervhub.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RichiestaContattoService {

    private final RichiestaContattoRepository richiestaRepository;
    private final UtenteRepository utenteRepository;
    private final BachecaService bachecaService; // Usiamo BachecaService per coerenza

    public RichiestaContattoService(RichiestaContattoRepository richiestaRepository,
                                    UtenteRepository utenteRepository,
                                    BachecaService bachecaService) {
        this.richiestaRepository = richiestaRepository;
        this.utenteRepository = utenteRepository;
        this.bachecaService = bachecaService;
    }

    @Transactional
    public void inviaRichiesta(String emailMittente, InviaRichiestaDTO dto) {

        // 1. Recupero Utente
        Utente allievo = utenteRepository.findByEmail(emailMittente);
        if (allievo == null) throw new IllegalArgumentException("Utente non trovato");

        // 2. Recupero Annuncio (Tramite Service)
        Annuncio annuncio = bachecaService.getAnnuncioById(dto.getIdAnnuncio());
        if (annuncio == null) throw new IllegalArgumentException("Annuncio non trovato");

        Utente tutor = annuncio.getAutore();

        // 3. Validazioni
        if (tutor.getIdUtente().equals(allievo.getIdUtente())) {
            throw new IllegalArgumentException("Non puoi contattare te stesso.");
        }

        if (richiestaRepository.existsByAllievoAndAnnuncioAndStato(allievo, annuncio, StatoRichiesta.IN_ATTESA)) {
            throw new IllegalArgumentException("Hai già una richiesta in attesa.");
        }

        // 4. Salvataggio
        RichiestaContatto richiesta = new RichiestaContatto();
        richiesta.setMessaggio(dto.getMessaggio());
        richiesta.setData(LocalDateTime.now());
        richiesta.setStato(StatoRichiesta.IN_ATTESA);
        richiesta.setAllievo(allievo);
        richiesta.setTutor(tutor);
        richiesta.setAnnuncio(annuncio);

        richiestaRepository.save(richiesta);
    }

    // ... (gli altri metodi findRichiesteRicevute rimangono uguali)
    public List<RichiestaContatto> findRichiesteRicevute(String emailTutor) {
        Utente tutor = utenteRepository.findByEmail(emailTutor);
        if(tutor == null) return List.of();
        return richiestaRepository.findAllByTutorOrderByDataDesc(tutor);
    }

    public List<RichiestaContatto> findRichiesteInviate(String emailAllievo) {
        Utente allievo = utenteRepository.findByEmail(emailAllievo);
        if(allievo == null) return List.of();
        return richiestaRepository.findAllByAllievoOrderByDataDesc(allievo);
    }

    @Transactional
    public void aggiornaStatoRichiesta(Long idRichiesta, String nuovoStatoString, String emailTutor) {
        RichiestaContatto richiesta = richiestaRepository.findById(idRichiesta)
                .orElseThrow(() -> new IllegalArgumentException("Richiesta non trovata"));

        if (!richiesta.getTutor().getEmail().equals(emailTutor)) {
            throw new SecurityException("Non autorizzato");
        }

        richiesta.setStato(StatoRichiesta.valueOf(nuovoStatoString.toUpperCase()));
        richiestaRepository.save(richiesta);
    }
}