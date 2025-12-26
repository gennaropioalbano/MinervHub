package it.minervhub.model;

import it.minervhub.config.StringListConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity che rappresenta un annuncio pubblicato da un utente.
 * Un annuncio contiene informazioni relative all'esame,
 * alla disponibilità, alla tariffa oraria e ad eventuali
 * condizioni di scambio.
 */
@Entity
@Table(name = "annuncio")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Annuncio {

    /**
     * Identificativo univoco dell'annuncio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    /**
     * Titolo dell'annuncio.
     */
    @Column(name = "titolo", length = 50, nullable = false)
    private String titolo;

    /**
     * Descrizione dell'annuncio.
     */
    @Column(name = "descrizione", length = 150, nullable = false)
    private String descrizione;

    /**
     * Indica se l'annuncio è attualmente disponibile.
     */
    @Column(name = "disponibile", nullable = false)
    private boolean disponibile;

    /**
     * Nome dell'esame associato all'annuncio.
     */
    @Column(name = "esame", length = 50, nullable = false)
    private String esame;

    /**
     * Tariffa oraria richiesta per il servizio.
     */
    @Column(name = "tariffa_oraria", nullable = false)
    private Integer tariffaOraria;

    /**
     * Lista di condizioni di scambio associate all'annuncio.
     * Viene salvata nel database come stringa tramite un converter.
     */
    @Column(name = "scambio", length = 255)
    @Convert(converter = StringListConverter.class)
    private List<String> scambio;

    /**
     * Corso di laurea associato all'annuncio.
     */
    @Column(name = "corso_laurea", length = 50)
    private String corsoLaurea;

    /**
     * Utente autore dell'annuncio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autore_id", nullable = false)
    private Utente autore;

    /**
     * Costruttore vuoto richiesto da JPA.
     */
    public Annuncio() {
    }

    /**
     * Costruttore completo per la creazione di un annuncio.
     *
     * @param titolo titolo dell'annuncio
     * @param descrizione descrizione dell'annuncio
     * @param disponibile stato di disponibilità
     * @param esame esame associato
     * @param tariffaOraria tariffa oraria richiesta
     * @param scambio condizioni di scambio
     * @param corsoLaurea corso di laurea
     * @param autore autore dell'annuncio
     */
    public Annuncio(String titolo, String descrizione, boolean disponibile, String esame, Integer tariffaOraria, List<String> scambio, String corsoLaurea, Utente autore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.disponibile = disponibile;
        this.esame = esame;
        this.tariffaOraria = tariffaOraria;
        this.scambio = scambio;
        this.corsoLaurea = corsoLaurea;
        this.autore = autore;
    }

    /** @return il titolo dell'annuncio */
    public String getTitolo() {
        return titolo;
    }

    /** @param titolo nuovo titolo dell'annuncio */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /** @return la descrizione dell'annuncio */
    public String getDescrizione() {
        return descrizione;
    }

    /** @param descrizione nuova descrizione dell'annuncio */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /** @return l'identificativo dell'annuncio */
    public Long getId() {
        return id;
    }

    /** @param id nuovo identificativo dell'annuncio */
    public void setId(Long id) {
        this.id = id;
    }

    /** @return true se l'annuncio è disponibile, false altrimenti */
    public boolean isDisponibile() {
        return disponibile;
    }

    /** @param disponibile nuovo stato di disponibilità */
    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    /** @return l'esame associato all'annuncio */
    public String getEsame() {
        return esame;
    }

    /** @param esame nuovo esame associato */
    public void setEsame(String esame) {
        this.esame = esame;
    }

    /** @return la tariffa oraria */
    public Integer getTariffaOraria() {
        return tariffaOraria;
    }

    /** @param tariffaOraria nuova tariffa oraria */
    public void setTariffaOraria(Integer tariffaOraria) {
        this.tariffaOraria = tariffaOraria;
    }

    /** @return lista delle condizioni di scambio */
    public List<String> getScambio() {
        return scambio;
    }

    /** @param scambio nuove condizioni di scambio */
    public void setScambio(List<String> scambio) {
        this.scambio = scambio;
    }

    /** @return il corso di laurea */
    public String getCorsoLaurea() {
        return corsoLaurea;
    }

    /** @param corsoLaurea nuovo corso di laurea */
    public void setCorsoLaurea(String corsoLaurea) {
        this.corsoLaurea = corsoLaurea;
    }

    /** @return l'autore dell'annuncio */
    public Utente getAutore() {
        return autore;
    }

    /** @param autore nuovo autore dell'annuncio */
    public void setAutore(Utente autore) {
        this.autore = autore;
    }
}