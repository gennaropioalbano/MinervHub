package it.minervhub.service;

import it.minervhub.exceptions.AnnuncioException;
import it.minervhub.model.Annuncio;
import it.minervhub.model.AnnuncioDto;
import it.minervhub.model.Utente;
import it.minervhub.repository.AnnuncioRepository;
import it.minervhub.repository.UtenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnuncioServiceTest {

    @Mock
    private AnnuncioRepository annuncioRepository;

    @Mock
    private UtenteRepository utenteRepository;

    @InjectMocks
    private AnnuncioService annuncioService;

    private Utente utente;
    private Annuncio annuncio;
    private AnnuncioDto dto;

    @BeforeEach
    void setup() {
        utente = new Utente();
        utente.setEmail("test@example.com");

        annuncio = new Annuncio();
        annuncio.setId(1L);
        annuncio.setAutore(utente);

        dto = new AnnuncioDto();
        dto.setTitolo("Titolo valido");
        dto.setDescrizione("Descrizione valida");
        dto.setEsame("Esame valido");
        dto.setCorsoLaurea("Corso valido");
        dto.setTariffaOraria(20);
        dto.setScambio("Libro1, Libro2");
    }

    @Test
    void testCreaAnnuncioConUtenteNonEsistente() {
        when(utenteRepository.findByEmail("nonexistent@example.com")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> annuncioService.creaAnnuncio(dto, "nonexistent@example.com"));
    }

    @Test
    void testModificaAnnuncioValido() {
        when(annuncioRepository.findById(1L)).thenReturn(Optional.of(annuncio));
        annuncioService.modificaAnnuncio(1L, dto, "test@example.com");
        verify(annuncioRepository).save(any(Annuncio.class));
        assertEquals("Titolo valido", annuncio.getTitolo());
    }

    @Test
    void testModificaAnnuncioNonAutorizzato() {
        when(annuncioRepository.findById(1L)).thenReturn(Optional.of(annuncio));
        assertThrows(RuntimeException.class,
                () -> annuncioService.modificaAnnuncio(1L, dto, "altro@example.com"));
    }

    @Test
    void testModificaAnnuncioTitoloTroppoLungo() {
        when(annuncioRepository.findById(1L)).thenReturn(Optional.of(annuncio));
        dto.setTitolo("T".repeat(51)); // oltre 50 caratteri
        assertThrows(AnnuncioException.class,
                () -> annuncioService.modificaAnnuncio(1L, dto, "test@example.com"));
    }

    @Test
    void testModificaAnnuncioDescrizioneVuota() {
        when(annuncioRepository.findById(1L)).thenReturn(Optional.of(annuncio));
        dto.setDescrizione("");
        assertThrows(AnnuncioException.class,
                () -> annuncioService.modificaAnnuncio(1L, dto, "test@example.com"));
    }

    @Test
    void testFindByAutoreEmailUtenteEsistente() {
        when(utenteRepository.findByEmail("test@example.com")).thenReturn(utente);
        when(annuncioRepository.findByAutore(utente)).thenReturn(List.of(annuncio));
        List<Annuncio> result = annuncioService.findByAutoreEmail("test@example.com");
        assertEquals(1, result.size());
    }

    @Test
    void testFindByAutoreEmailUtenteNonEsistente() {
        when(utenteRepository.findByEmail("nonexistent@example.com")).thenReturn(null);
        List<Annuncio> result = annuncioService.findByAutoreEmail("nonexistent@example.com");
        assertTrue(result.isEmpty());
    }

    @Test
    void testEliminaAnnuncioEsistente() {
        when(annuncioRepository.existsById(1L)).thenReturn(true);
        annuncioService.eliminaAnnuncio(1L);
        verify(annuncioRepository).deleteById(1L);
    }

    @Test
    void testEliminaAnnuncioNonEsistente() {
        when(annuncioRepository.existsById(2L)).thenReturn(false);
        annuncioService.eliminaAnnuncio(2L);
        verify(annuncioRepository, never()).deleteById(2L);
    }
}