package it.minervhub.model;

import jakarta.validation.constraints.*;

/**
 * Data Transfer Object utilizzato per la creazione e la modifica
 * degli annunci.
 * Contiene esclusivamente i dati inseriti dall'utente e le relative
 * regole di validazione.
 */
public class AnnuncioDto {

    /**
     * Identificativo dell'annuncio.
     * Utilizzato principalmente nelle operazioni di modifica.
     */
    private Long id;

    /**
     * Titolo dell'annuncio.
     */
    @NotBlank(message = "Il titolo è obbligatorio")
    @Size(max = 50, message = "Il titolo è troppo lungo")
    private String titolo;

    /**
     * Descrizione dell'annuncio.
     */
    @NotBlank(message = "La descrizione è obbligatoria")
    @Size(max = 150, message = "La descrizione è troppo lunga")
    private String descrizione;

    /**
     * Esame associato all'annuncio.
     */
    @NotBlank(message = "L'esame è obbligatorio")
    @Size(max = 50, message = "L'esame è troppo lungo")
    private String esame;

    /**
     * Corso di laurea associato all'annuncio.
     */
    @NotBlank(message = "Il corso è obbligatorio")
    @Size(max = 50, message = "Il corso è troppo lungo")
    private String corsoLaurea;

    /**
     * Tariffa oraria richiesta per il servizio.
     */
    @NotNull(message = "La tariffa è obbligatoria")
    @Min(value = 5, message = "La tariffa minima è 5€")
    @Max(value = 50, message = "La tariffa massima è 50€")
    private Integer tariffaOraria;

    /**
     * Eventuali condizioni di scambio inserite dall'utente.
     */
    @Size(max = 150, message = "Il campo scambio è troppo lungo")
    private String scambio;

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getEsame() {
        return esame;
    }

    public void setEsame(String esame) {
        this.esame = esame;
    }

    public String getCorsoLaurea() {
        return corsoLaurea;
    }

    public void setCorsoLaurea(String corsoLaurea) {
        this.corsoLaurea = corsoLaurea;
    }

    public Integer getTariffaOraria() {
        return tariffaOraria;
    }

    public void setTariffaOraria(Integer tariffaOraria) {
        this.tariffaOraria = tariffaOraria;
    }

    public String getScambio() {
        return scambio;
    }

    public void setScambio(String scambio) {
        this.scambio = scambio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
