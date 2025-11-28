package it.minervhub.repository;

import it.minervhub.model.Annuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnuncioRepository extends JpaRepository<Annuncio, Long> {
}
