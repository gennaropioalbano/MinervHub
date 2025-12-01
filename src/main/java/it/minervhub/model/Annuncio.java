package it.minervhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "annuncio")
public class Annuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titolo", length = 50, nullable = false)
    private String titolo;

    @Column(name = "descrizione", length = 150, nullable = false)
    private String descrizione;

    @Column(name = "esame", length = 30, nullable = false)
    private String esame;

    @Column(name = "tariffaOraria", nullable = false)
    private Integer tariffaOraria;

    @Column(name = "scambioLezioni", length = 150)
    private String scambioLezioni;

    @Column(name = "corsoLaurea", length = 30)
    private String corsoLaurea;

    @ManyToOne
    @JoinColumn(name = "autore", nullable = false)
    private Utente autore;
}