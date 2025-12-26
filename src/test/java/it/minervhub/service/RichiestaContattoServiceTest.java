package it.minervhub.service;

import it.minervhub.model.*;
import it.minervhub.repository.RichiestaContattoRepository;
import it.minervhub.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RichiestaContattoServiceTest {

    @Mock
    private RichiestaContattoRepository richiestaRepository;

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private BachecaService bachecaService;

    @InjectMocks
    private RichiestaContattoService richiestaContattoService;

    private Utente allievo;
    private Utente tutor;
    private Annuncio annuncio;
    private InviaRichiestaDTO richiestaDTO;

    @BeforeEach
    void setup() {
        // Setup Dati Base (Necessari per far girare il metodo senza NullPointerException)
        allievo = new Utente();
        allievo.setIdUtente(1L);
        allievo.setEmail("studente@test.it");

        tutor = new Utente();
        tutor.setIdUtente(2L); // ID diverso per evitare errore auto-invio (se presente nel service)
        tutor.setEmail("tutor@test.it");

        annuncio = new Annuncio();
        annuncio.setId(100L);
        annuncio.setAutore(tutor);

        richiestaDTO = new InviaRichiestaDTO();
        richiestaDTO.setIdAnnuncio(100L);

        // Configuriamo i mock comuni a tutti i test per superare le righe di recupero dati
        lenient().when(utenteRepository.findByEmail("studente@test.it")).thenReturn(allievo);
        lenient().when(bachecaService.getAnnuncioById(100L)).thenReturn(annuncio);
        // Simuliamo che non ci siano richieste precedenti per evitare blocchi non oggetto del test
        lenient().when(richiestaRepository.existsByAllievoAndAnnuncioAndStato(any(), any(), any())).thenReturn(false);
    }

    // ==========================================
    // TEST CASE SPECIFICATION: INVIO RICHIESTA
    // ==========================================

    @Test
    void TC_3_1_MessaggioTroppoLungo() {
        // SCENARIO: Il messaggio supera i 200 caratteri.
        // ORACOLO: L'invio non va a buon fine (Eccezione).

        String messaggioLungo = "A".repeat(201); // Genera una stringa di 201 caratteri
        richiestaDTO.setMessaggio(messaggioLungo);

        assertThrows(
                IllegalArgumentException.class,
                () -> richiestaContattoService.inviaRichiesta("studente@test.it", richiestaDTO),
                "Il test deve fallire se il messaggio supera i 200 caratteri"
        );

        // Verifica che NON venga salvato nulla nel DB
        verify(richiestaRepository, never()).save(any());
    }

    @Test
    void TC_3_2_MessaggioVuoto() {
        // SCENARIO: Il messaggio è vuoto (lunghezza 0).
        // ORACOLO: L'invio va a buon fine (Parametro facoltativo).

        richiestaDTO.setMessaggio("");

        // Eseguiamo il metodo
        assertDoesNotThrow(() ->
                richiestaContattoService.inviaRichiesta("studente@test.it", richiestaDTO)
        );

        // Verifica che il salvataggio sia avvenuto
        verify(richiestaRepository, times(1)).save(any(RichiestaContatto.class));
    }

    @Test
    void TC_3_3_MessaggioValido() {
        // SCENARIO: Il messaggio ha una lunghezza valida (tra 1 e 200).
        // ORACOLO: L'invio va a buon fine.

        richiestaDTO.setMessaggio("Salve, vorrei informazioni sulle lezioni."); // ~45 caratteri

        // Eseguiamo il metodo
        assertDoesNotThrow(() ->
                richiestaContattoService.inviaRichiesta("studente@test.it", richiestaDTO)
        );

        // Verifica che il salvataggio sia avvenuto con i dati corretti
        verify(richiestaRepository, times(1)).save(argThat(richiesta ->
                richiesta.getMessaggio().equals("Salve, vorrei informazioni sulle lezioni.") &&
                        richiesta.getAllievo().equals(allievo) &&
                        richiesta.getAnnuncio().equals(annuncio)
        ));
    }
}