package it.minervhub.repository;

import it.minervhub.model.Annuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnuncioRepository extends JpaRepository<Annuncio, Long> {
    List<Annuncio> findByDisponibileTrue();

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
}
