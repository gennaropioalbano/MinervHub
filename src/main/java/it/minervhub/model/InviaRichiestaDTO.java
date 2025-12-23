package it.minervhub.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class InviaRichiestaDTO {

    @NotNull(message = "L'ID dell'annuncio è obbligatorio")
    private Long idAnnuncio;

    @Size(max = 500, message = "Il messaggio non può superare i 500 caratteri")
    private String messaggio;

    public Long getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(Long idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}