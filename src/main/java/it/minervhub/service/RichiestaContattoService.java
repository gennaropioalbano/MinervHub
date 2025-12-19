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

    // ... altri metodi ...

    @Transactional
    public void gestisciRichiesta(Long idRichiesta, String emailTutor, String nuovoStato, String messaggioRisposta) {
        // 1. Recupera la richiesta
        RichiestaContatto richiesta = richiestaRepository.findById(idRichiesta)
                .orElseThrow(() -> new IllegalArgumentException("Richiesta non trovata"));

        // 2. Sicurezza: controlla che sia davvero il tutor di questa richiesta a rispondere
        if (!richiesta.getTutor().getEmail().equals(emailTutor)) {
            throw new SecurityException("Non sei autorizzato a gestire questa richiesta.");
        }

        // 3. Aggiorna lo stato
        try {
            richiesta.setStato(StatoRichiesta.valueOf(nuovoStato));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Stato non valido: " + nuovoStato);
        }

        // 4. Salva la risposta (Se presente nell'entità come hai detto)
        // Assumo che il campo nella tua Entity si chiami "risposta" o simile.
        // Se si chiama diversamente, cambia .setRisposta(...)
        if (messaggioRisposta != null && !messaggioRisposta.isBlank()) {
            richiesta.setRisposta(messaggioRisposta);
        }

        richiestaRepository.save(richiesta);
    }
}