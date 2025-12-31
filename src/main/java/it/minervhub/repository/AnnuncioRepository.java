package it.minervhub.repository;

import it.minervhub.model.Annuncio;
import it.minervhub.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repository per la gestione della persistenza degli Annunci.
 * Fornisce metodi di accesso ai dati basati su Spring Data JPA.
 */
@Repository
public interface AnnuncioRepository extends JpaRepository<Annuncio, Long> {
    List<Annuncio> findByAutore(Utente autore);

    List<Annuncio> findByDisponibileTrue();

    /**
     * Filtra gli annunci disponibili in base ai criteri forniti.
     * I parametri null non vengono considerati nel filtraggio.
     *
     * @param corsoLaurea corso di laurea su cui filtrare (opzionale)
     * @param esame nome dell'esame su cui filtrare (opzionale)
     * @param tariffaMax tariffa oraria massima consentita (opzionale)
     * @return lista di annunci che soddisfano i criteri
     */
    @Query("""
        SELECT a FROM Annuncio a
        WHERE a.disponibile = true
        AND (:corsoLaurea IS NULL OR a.corsoLaurea = :corsoLaurea)
        AND (:esame IS NULL OR a.esame = :esame)
        AND (:tariffaMax IS NULL OR a.tariffaOraria <= :tariffaMax)
    """)
    List<Annuncio> filtraAnnunci(
            @Param("corsoLaurea") String corsoLaurea,
            @Param("esame") String esame,
            @Param("tariffaMax") Integer tariffaMax
    );


    List<Annuncio> searchAnnuncioById(Long id);

    List<Annuncio> findByAutoreAndDisponibile(Utente autore, boolean disponibile);

}
