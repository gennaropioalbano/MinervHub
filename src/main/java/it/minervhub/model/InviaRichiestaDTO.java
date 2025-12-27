package it.minervhub.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) utilizzato per raccogliere i dati inviati dal client
 * quando un utente desidera effettuare una nuova richiesta di contatto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviaRichiestaDTO {

    /**
     * L'identificativo univoco dell'annuncio a cui l'utente sta rispondendo.
     * Questo campo è obbligatorio per associare la richiesta all'annuncio corretto.
     */
    @NotNull(message = "L'ID dell'annuncio è obbligatorio")
    private Long idAnnuncio;

    /**
     * Testo opzionale inviato dall'utente insieme alla richiesta.
     * La lunghezza massima consentita è di 500 caratteri.
     */
    @Size(max = 500, message = "Il messaggio non può superare i 500 caratteri")
    private String messaggio;

    public @Size(max = 500, message = "Il messaggio non può superare i 500 caratteri") String getMessaggio() {
        return messaggio;
    }

    public @NotNull(message = "L'ID dell'annuncio è obbligatorio") Long getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(@NotNull(message = "L'ID dell'annuncio è obbligatorio") Long idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    public void setMessaggio(@Size(max = 500, message = "Il messaggio non può superare i 500 caratteri") String messaggio) {
        this.messaggio = messaggio;
    }
}