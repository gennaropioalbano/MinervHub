package it.minervhub.service;

import it.minervhub.exceptions.AnnuncioException;
import it.minervhub.model.*;
import it.minervhub.repository.AnnuncioRepository;
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
class AnnuncioServiceTest {

    @Mock
    private AnnuncioRepository annuncioRepository;

    @Mock
    private UtenteRepository utenteRepository;

    @InjectMocks
    private AnnuncioService annuncioService;

    private Utente autore;
    private AnnuncioDTO dto;

    @BeforeEach
    void setup() {
        autore = new Utente();
        autore.setIdUtente(1L);
        autore.setEmail("tutor@test.it");

        dto = new AnnuncioDTO();
        dto.setTitolo("Ripetizioni Analisi 1");
        dto.setDescrizione("Offro ripetizioni mirate di Analisi 1 per studenti di Informatica.");
        dto.setEsame("Analisi 1");
        dto.setCorsoLaurea("Informatica");
        dto.setTariffaOraria(15);
        dto.setScambio("Programmazione 1, Basi di Dati");

        lenient().when(utenteRepository.findByEmail("tutor@test.it")).thenReturn(autore);
    }

    // ==========================================
    // TEST CASE SPECIFICATION: CREAZIONE ANNUNCIO
    // ==========================================

    @Test
    void TC_1_TitoloObbligatorio() {
        dto.setTitolo("");

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_2_TitoloTroppoLungo() {
        dto.setTitolo("Ripetizioni complete e approfondite di Analisi Matematica 1 per studenti universitari di Informatica");

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_3_DescrizioneObbligatoria() {
        dto.setDescrizione("");

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_4_DescrizioneTroppoLunga() {
        dto.setDescrizione("""
                Offro ripetizioni dettagliate di Analisi Matematica 1 rivolte a studenti del corso di laurea in Informatica.
                Le lezioni includono spiegazioni teoriche approfondite, svolgimento guidato di esercizi, chiarimenti sui teoremi
                principali e simulazioni d’esame per una preparazione completa allo scritto e all’orale.
                """);

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_5_EsameObbligatorio() {
        dto.setEsame("");

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_6_EsameTroppoLungo() {
        dto.setEsame("Analisi matematica 1 – Limiti, derivate, integrali e successioni");

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_7_CorsoDiLaureaObbligatorio() {
        dto.setCorsoLaurea("");

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_8_CorsoDiLaureaTroppoLungo() {
        dto.setCorsoLaurea("Laurea Triennale in Informatica e discipline informatiche affini");

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_9_TariffaTroppoBassa() {
        dto.setTariffaOraria(4);

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_10_TariffaTroppoAlta() {
        dto.setTariffaOraria(60);

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_11_ScambioTroppoLungo() {
        dto.setScambio("Programmazione 1, Progettazione di Algoritmi, Basi di Dati, Sistemi Operativi, " +
                "Ingegneria del Software, Programmazione e Strutture Dati, Reti di Calcolatori");

        assertThrows(AnnuncioException.class,
                () -> annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, never()).save(any());
    }

    @Test
    void TC_12_InserimentoOK_ScambioVuoto() {
        dto.setScambio("");

        assertDoesNotThrow(() ->
                annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, times(1)).save(any(Annuncio.class));
    }

    @Test
    void TC_13_InserimentoOK_ConScambio() {
        assertDoesNotThrow(() ->
                annuncioService.creaAnnuncio(dto, "tutor@test.it"));

        verify(annuncioRepository, times(1)).save(argThat(annuncio ->
                annuncio.getTitolo().equals(dto.getTitolo()) &&
                        annuncio.getAutore().equals(autore)
        ));
    }
}
