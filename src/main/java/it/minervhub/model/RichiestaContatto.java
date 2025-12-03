package it.minervhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "richiesta_contatto")
public class RichiestaContatto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "messaggio", length = 200, nullable = false)
    private String messaggio;

    @Column(name = "risposta", length = 200)
    private String risposta;

    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato", nullable = false)
    private StatoRichiesta stato;

    @ManyToOne
    @JoinColumn(name = "allievo", referencedColumnName = "id", nullable = false)
    private Utente allievo;

    @ManyToOne
    @JoinColumn(name = "tutor", referencedColumnName = "id", nullable = false)
    private Utente tutor;

    @ManyToOne
    @JoinColumn(name = "idAnnuncio", nullable = false)
    private Annuncio annuncio;
}
