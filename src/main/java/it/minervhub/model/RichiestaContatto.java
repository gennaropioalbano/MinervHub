package it.minervhub.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entità JPA che rappresenta una richiesta di contatto formale tra un Allievo e un Tutor.
 * Mappa la tabella "richiesta_contatto" nel database.
 * <p>
 * Questa classe gestisce il ciclo di vita della richiesta, tracciando chi l'ha inviata,
 * a chi è diretta, per quale annuncio, il messaggio iniziale e l'eventuale risposta.
 */
@Entity
@Table(name = "richiesta_contatto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RichiestaContatto {

    /**
     * Identificativo univoco della richiesta (Primary Key).
     * Generato automaticamente dal database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Messaggio introduttivo inviato dall'allievo al momento della richiesta.
     * La lunghezza massima è di 200 caratteri.
     */
    @Column(name = "messaggio", length = 200)
    private String messaggio;

    /**
     * Risposta inviata dal tutor all'allievo (opzionale in fase di creazione).
     * La lunghezza massima è di 200 caratteri.
     */
    @Column(name = "risposta", length = 200)
    private String risposta;

    /**
     * Timestamp che indica la data e l'ora in cui la richiesta è stata creata.
     * Non può essere nullo.
     */
    @Column(name = "data", nullable = false)
    private LocalDateTime data;

    /**
     * Stato corrente della richiesta (es. IN_ATTESA, ACCETTATA, RIFIUTATA).
     * Salvato come stringa nel database.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "stato", nullable = false)
    private StatoRichiesta stato;

    /**
     * L'utente (con ruolo Allievo) che ha avviato la richiesta di contatto.
     * Relazione Many-to-One: un allievo può fare molte richieste.
     */
    @ToString.Exclude // Evita loop infiniti nel toString()
    @ManyToOne
    @JoinColumn(name = "allievo", referencedColumnName = "id", nullable = false)
    private Utente allievo;

    /**
     * L'utente (con ruolo Tutor) destinatario della richiesta.
     * Relazione Many-to-One: un tutor può ricevere molte richieste.
     */
    @ToString.Exclude // Evita loop infiniti nel toString()
    @ManyToOne
    @JoinColumn(name = "tutor", referencedColumnName = "id", nullable = false)
    private Utente tutor;

    /**
     * L'annuncio specifico per il quale viene fatta la richiesta.
     * Relazione Many-to-One: un annuncio può ricevere molte richieste.
     */
    @ToString.Exclude // Evita loop infiniti nel toString()
    @ManyToOne
    @JoinColumn(name = "idAnnuncio", nullable = false)
    private Annuncio annuncio;

    public void setAllievo(Utente allievo) {
        this.allievo = allievo;
    }

    public void setAnnuncio(Annuncio annuncio) {
        this.annuncio = annuncio;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public void setRisposta(String risposta) {
        this.risposta = risposta;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    public void setTutor(Utente tutor) {
        this.tutor = tutor;
    }

    public Utente getAllievo() {
        return allievo;
    }

    public Annuncio getAnnuncio() {
        return annuncio;
    }

    public LocalDateTime getData() {
        return data;
    }

    public Long getId() {
        return id;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public String getRisposta() {
        return risposta;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public Utente getTutor() {
        return tutor;
    }
}