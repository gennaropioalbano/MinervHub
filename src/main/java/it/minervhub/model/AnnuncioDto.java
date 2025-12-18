package it.minervhub.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


public class AnnuncioDto {
    private Long id;

    @NotEmpty(message = "The name is required")
    private String titolo;

    @NotEmpty(message = "The description is required")
    private String descrizione;

    @NotEmpty(message = " The esame is required")
    private String esame;

    @NotEmpty(message = "The corso is required")
    private String corsoLaurea;

    @Min(5)
    private Integer tariffaOraria;

    private String scambio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotEmpty(message = "The name is required") String getTitolo() {
        return titolo;
    }

    public void setTitolo(@NotEmpty(message = "The name is required") String titolo) {
        this.titolo = titolo;
    }

    public @NotEmpty(message = "The description is required") String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(@NotEmpty(message = "The description is required") String descrizione) {
        this.descrizione = descrizione;
    }

    public @NotEmpty(message = " The esame is required") String getEsame() {
        return esame;
    }

    public void setEsame(@NotEmpty(message = " The esame is required") String esame) {
        this.esame = esame;
    }

    public @NotEmpty(message = "The corso is required") String getCorsoLaurea() {
        return corsoLaurea;
    }

    public void setCorsoLaurea(@NotEmpty(message = "The corso is required") String corsoLaurea) {
        this.corsoLaurea = corsoLaurea;
    }

    public @Min(5) Integer getTariffaOraria() {
        return tariffaOraria;
    }

    public void setTariffaOraria(@Min(5) Integer tariffaOraria) {
        this.tariffaOraria = tariffaOraria;
    }

    public String getScambio() {
        return scambio;
    }

    public void setScambio(String scambio) {
        this.scambio = scambio;
    }
}
