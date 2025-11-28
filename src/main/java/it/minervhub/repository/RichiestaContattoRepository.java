package it.minervhub.repository;

import it.minervhub.model.RichiestaContatto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RichiestaContattoRepository extends JpaRepository<RichiestaContatto, Long> {
}
