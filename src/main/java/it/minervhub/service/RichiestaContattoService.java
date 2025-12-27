package it.minervhub.service;

import it.minervhub.model.InviaRichiestaDTO;
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

/**
 * Service che gestisce la logica di business relativa alle richieste di contatto
 * tra studenti (allievi) e tutor.
 * Si occupa della creazione, recupero e aggiornamento delle richieste.
 */
@Service
public class RichiestaContattoService {

    private final RichiestaContattoRepository richiestaRepository;
    private final UtenteRepository utenteRepository;
    private final BachecaService bachecaService;

    /**
     * Costruttore per l'iniezione delle dipendenze.
     *
     * @param richiestaRepository Repository per l'accesso ai dati delle richieste.
     * @param utenteRepository    Repository per l'accesso ai dati degli utenti.
     * @param bachecaService      Service per il recupero delle informazioni sugli annunci.
     */
    public RichiestaContattoService(RichiestaContattoRepository richiestaRepository,
                                    UtenteRepository utenteRepository,
                                    BachecaService bachecaService) {
        this.richiestaRepository = richiestaRepository;
        this.utenteRepository = utenteRepository;
        this.bachecaService = bachecaService;
    }

    /**
     * Crea e invia una nuova richiesta di contatto da parte di uno studente per un determinato annuncio.
     * Effettua controlli di validazione per evitare auto-invii o richieste duplicate in attesa.
     *
     * @param emailMittente L'email dell'utente che sta inviando la richiesta (Allievo).
     * @param dto           Oggetto DTO contenente l'ID dell'annuncio e il messaggio di presentazione.
     * @throws IllegalArgumentException Se l'utente o l'annuncio non esistono, se si tenta di contattare se stessi
     * o se esiste già una richiesta in attesa.
     */
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

        if (dto.getMessaggio() != null && dto.getMessaggio().length() > 200) {
            throw new IllegalArgumentException("Il messaggio non può superare i 200 caratteri.");
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

    /**
     * Recupera la lista delle richieste di contatto ricevute da un tutor.
     *
     * @param emailTutor L'email del tutor.
     * @return Una lista di richieste ricevute, ordinate per data decrescente.
     */
    public List<RichiestaContatto> findRichiesteRicevute(String emailTutor) {
        Utente tutor = utenteRepository.findByEmail(emailTutor);
        if(tutor == null) return List.of();
        return richiestaRepository.findAllByTutorOrderByDataDesc(tutor);
    }

    /**
     * Recupera la lista delle richieste di contatto inviate da uno studente.
     *
     * @param emailAllievo L'email dello studente.
     * @return Una lista di richieste inviate, ordinate per data decrescente.
     */
    public List<RichiestaContatto> findRichiesteInviate(String emailAllievo) {
        Utente allievo = utenteRepository.findByEmail(emailAllievo);
        if(allievo == null) return List.of();
        return richiestaRepository.findAllByAllievoOrderByDataDesc(allievo);
    }

    /**
     * Aggiorna esclusivamente lo stato di una richiesta (es. ACCETTATA o DECLINATA).
     *
     * @param idRichiesta      L'ID univoco della richiesta.
     * @param nuovoStatoString La stringa che rappresenta il nuovo stato (es. "ACCETTATA").
     * @param emailTutor       L'email del tutor che sta eseguendo l'azione (per verifica di sicurezza).
     * @throws IllegalArgumentException Se la richiesta non esiste o lo stato non è valido.
     * @throws SecurityException        Se l'utente che tenta l'aggiornamento non è il tutor destinatario.
     */
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

    /**
     * Gestisce una richiesta ricevuta, permettendo di aggiornare lo stato e inserire una risposta testuale.
     *
     * @param idRichiesta       L'ID univoco della richiesta.
     * @param emailTutor        L'email del tutor che sta gestendo la richiesta.
     * @param nuovoStato        Il nuovo stato da assegnare (ACCETTATA o DECLINATA).
     * @param messaggioRisposta Un messaggio opzionale di risposta allo studente.
     * @throws IllegalArgumentException Se la richiesta non esiste o lo stato non è valido.
     * @throws SecurityException        Se l'utente che tenta l'operazione non è il tutor destinatario.
     */
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
        if (messaggioRisposta != null && !messaggioRisposta.isBlank()) {
            richiesta.setRisposta(messaggioRisposta);
        }

        richiestaRepository.save(richiesta);
    }

    @Transactional
    public void annullaRichiesta(Long idRichiesta, String emailAllievo) {
        // 1. Recupera la richiesta
        RichiestaContatto richiesta = richiestaRepository.findById(idRichiesta)
                .orElseThrow(() -> new IllegalArgumentException("Richiesta non trovata"));

        // 2. Controllo Sicurezza: Solo chi l'ha creata può annullarla
        if (!richiesta.getAllievo().getEmail().equals(emailAllievo)) {
            throw new SecurityException("Non sei autorizzato ad annullare questa richiesta.");
        }

        // 3. Controllo Stato: Si può annullare solo se è ancora IN_ATTESA
        if (richiesta.getStato() != StatoRichiesta.IN_ATTESA) {
            throw new IllegalArgumentException("Non puoi annullare una richiesta già gestita (Accettata o Declinata).");
        }

        // 4. Eliminazione fisica dal database
        richiestaRepository.delete(richiesta);
    }


}