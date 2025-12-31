package it.minervhub.repository;

import it.minervhub.model.Annuncio;
import it.minervhub.model.RichiestaContatto;
import it.minervhub.model.StatoRichiesta;
import it.minervhub.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository per la gestione della persistenza delle richieste di contatto.
 * Estende {@link JpaRepository} per fornire operazioni CRUD standard.
 */
@Repository
public interface RichiestaContattoRepository extends JpaRepository<RichiestaContatto, Long> {

    /**
     * Recupera tutte le richieste di contatto ricevute da uno specifico Tutor.
     * I risultati sono ordinati in base alla data di creazione in ordine decrescente (dalla più recente alla più vecchia).
     *
     * @param tutor L'utente con ruolo Tutor di cui cercare le richieste ricevute.
     * @return Una lista di {@link RichiestaContatto} associate al tutor specificato.
     */
    List<RichiestaContatto> findAllByTutorOrderByDataDesc(Utente tutor);

    /**
     * Recupera tutte le richieste di contatto inviate da uno specifico Allievo.
     * I risultati sono ordinati in base alla data di creazione in ordine decrescente.
     *
     * @param allievo L'utente con ruolo Allievo che ha inviato le richieste.
     * @return Una lista di {@link RichiestaContatto} inviate dall'allievo specificato.
     */
    List<RichiestaContatto> findAllByAllievoOrderByDataDesc(Utente allievo);

    /**
     * Verifica l'esistenza di una richiesta di contatto specifica.
     * Utile per prevenire l'invio di richieste duplicate (spam) per lo stesso annuncio
     * quando esiste già una richiesta in un determinato stato (es. 'IN_ATTESA').
     *
     * @param allievo  L'allievo che sta tentando di inviare la richiesta.
     * @param annuncio L'annuncio a cui si riferisce la richiesta.
     * @param stato    Lo stato della richiesta da cercare (es. {@code StatoRichiesta.IN_ATTESA}).
     * @return {@code true} se esiste già una richiesta con questi criteri, {@code false} altrimenti.
     */
    boolean existsByAllievoAndAnnuncioAndStato(Utente allievo, Annuncio annuncio, StatoRichiesta stato);
}