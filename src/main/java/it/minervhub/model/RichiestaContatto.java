package it.minervhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "richiesta_contatto")
public class RichiestaContatto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "messaggio", length = 200)
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

    public RichiestaContatto() {
    }

    public RichiestaContatto(String messaggio, LocalDateTime data, String risposta, StatoRichiesta stato, Utente allievo, Utente tutor, Annuncio annuncio) {
        this.messaggio = messaggio;
        this.data = data;
        this.risposta = risposta;
        this.stato = stato;
        this.allievo = allievo;
        this.tutor = tutor;
        this.annuncio = annuncio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getRisposta() {
        return risposta;
    }

    public void setRisposta(String risposta) {
        this.risposta = risposta;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    public Utente getAllievo() {
        return allievo;
    }

    public void setAllievo(Utente allievo) {
        this.allievo = allievo;
    }

    public Utente getTutor() {
        return tutor;
    }

    public void setTutor(Utente tutor) {
        this.tutor = tutor;
    }

    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }
}
