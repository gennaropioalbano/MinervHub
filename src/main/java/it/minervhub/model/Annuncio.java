package it.minervhub.model;

import it.minervhub.config.StringListConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "annuncio")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Annuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "titolo", length = 50, nullable = false)
    private String titolo;

    @Column(name = "descrizione", length = 150, nullable = false)
    private String descrizione;

    @Column(name = "disponibile", nullable = false)
    private boolean disponibile;

    @Column(name = "esame", length = 30, nullable = false)
    private String esame;

    @Column(name = "tariffa_oraria", nullable = false)
    private Integer tariffaOraria;

    @Column(name = "scambio", length = 255)
    @Convert(converter = StringListConverter.class)
    private List<String> scambio;

    @Column(name = "corso_laurea", length = 30)
    private String corsoLaurea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autore_id", nullable = false)
    private Utente autore;
}