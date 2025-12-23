package it.minervhub.model;

import jakarta.validation.constraints.*;

public class AnnuncioDto {
    private Long id;

    @NotBlank(message = "Il titolo è obbligatorio")
    @Size(max = 50, message = "Il titolo è troppo lungo")
    private String titolo;

    @NotBlank(message = "La descrizione è obbligatoria")
    @Size(max = 150, message = "La descrizione è troppo lunga")
    private String descrizione;

    @NotBlank(message = "L'esame è obbligatorio")
    @Size(max = 50, message = "L'esame è troppo lungo")
    private String esame;

    @NotBlank(message = "Il corso è obbligatorio")
    @Size(max = 50, message = "Il corso è troppo lungo")
    private String corsoLaurea;

    @NotNull(message = "La tariffa è obbligatoria")
    @Min(value = 5, message = "La tariffa minima è 5€")
    @Max(value = 50, message = "La tariffa massima è 50€")
    private Integer tariffaOraria;

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
