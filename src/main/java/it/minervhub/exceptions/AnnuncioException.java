package it.minervhub.exceptions;

/**
 * Eccezione personalizzata per segnalare errori nella gestione degli annunci.
 * Viene lanciata quando i dati dell'annuncio non rispettano i vincoli o sono invalidi.
 */
public class AnnuncioException extends RuntimeException {

    /**
     * Costruttore base con messaggio
     *
     * @param message messaggio di errore
     */
    public AnnuncioException(String message) {
        super(message);
    }

    /**
     * Costruttore con messaggio e causa originale
     *
     * @param message messaggio di errore
     * @param cause causa originale dell'eccezione
     */
    public AnnuncioException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Costruttore con causa originale
     *
     * @param cause causa originale dell'eccezione
     */
    public AnnuncioException(Throwable cause) {
        super(cause);
    }
}