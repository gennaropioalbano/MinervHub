package it.minervhub.repository;

import it.minervhub.model.Annuncio;
import it.minervhub.model.RichiestaContatto;
import it.minervhub.model.StatoRichiesta;
import it.minervhub.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RichiestaContattoRepository extends JpaRepository<RichiestaContatto, Long> {
    // Trova le richieste ricevute (per il Tutor), ordinate per data decrescente
    List<RichiestaContatto> findAllByTutorOrderByDataDesc(Utente tutor);

    // Trova le richieste inviate (per l'Allievo), ordinate per data decrescente
    List<RichiestaContatto> findAllByAllievoOrderByDataDesc(Utente allievo);

    // Utile per evitare spam: controlla se esiste già una richiesta pendente tra questi due utenti per questo annuncio
    boolean existsByAllievoAndAnnuncioAndStato(Utente allievo, Annuncio annuncio, StatoRichiesta stato);
}
