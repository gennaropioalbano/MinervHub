package it.minervhub.service;

import it.minervhub.exceptions.InvalidFiltroException;
import it.minervhub.model.Annuncio;
import it.minervhub.repository.AnnuncioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BachecaServiceTest {

    @Mock
    private AnnuncioRepository annuncioRepository;

    @InjectMocks
    private BachecaService bachecaService;

    private Annuncio annuncio1;
    private Annuncio annuncio2;
    private Annuncio annuncio3;

    @BeforeEach
    void setup() {
        annuncio1 = new Annuncio();
        annuncio1.setCorsoLaurea("Informatica");
        annuncio1.setEsame("Matematica Discreta");
        annuncio1.setTariffaOraria(15);
        annuncio1.setDisponibile(true);

        annuncio2 = new Annuncio();
        annuncio2.setCorsoLaurea("Informatica");
        annuncio2.setEsame("Programmazione 1");
        annuncio2.setTariffaOraria(25);
        annuncio2.setDisponibile(true);

        annuncio3 = new Annuncio();
        annuncio3.setCorsoLaurea("Fisica");
        annuncio3.setEsame("Analisi 1");
        annuncio3.setTariffaOraria(15);
        annuncio3.setDisponibile(true);
    }

    // ======================
    // CASI DI ERRORE (2.1–2.3)
    // ======================

    @Test
    void TC1_EsameNonValido() {
        String esameLungo =
                "Storia della matematica aritmetica con cenni alla fisica applicata";

        assertThrows(
                InvalidFiltroException.class,
                () -> bachecaService.getAnnunciFiltrati(null, esameLungo, null)
        );
    }

    @Test
    void TC2_CorsoDiLaureaNonValido() {
        String corsoLungo =
                "Scienze delle tecnologie dell’informatica dell’informazione dei calcolatori";

        assertThrows(
                InvalidFiltroException.class,
                () -> bachecaService.getAnnunciFiltrati(corsoLungo, "Matematica Discreta", null)
        );
    }

    @Test
    void TC3_TariffaOrariaNonValida() {
        assertThrows(
                InvalidFiltroException.class,
                () -> bachecaService.getAnnunciFiltrati(
                        "Informatica",
                        "Matematica Discreta",
                        2
                )
        );
    }

    // ======================
    // CASI VALIDI (2.4–2.10)
    // ======================

    @Test
    void TC4_RicercaFiltriPerEsameCorsoLaureaTariffaOraria() {

        when(annuncioRepository.filtraAnnunci(
                "Informatica", "Matematica Discreta", 20))
                .thenReturn(List.of(annuncio1));

        List<Annuncio> result =
                bachecaService.getAnnunciFiltrati(
                        "Informatica", "Matematica Discreta", 20);

        assertEquals(1, result.size());
        assertEquals("Matematica Discreta", result.get(0).getEsame());
    }

    @Test
    void TC5_RicercaFiltriPerCorsoLaureaTariffaOraria() {

        when(annuncioRepository.filtraAnnunci(
                "Informatica", null, 20))
                .thenReturn(List.of(annuncio1, annuncio2));

        List<Annuncio> result =
                bachecaService.getAnnunciFiltrati("Informatica", null, 20);

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(a -> a.getCorsoLaurea().equals("Informatica")));
    }

    @Test
    void TC6_RicercaFiltriPerEsameTariffaOraria() {

        when(annuncioRepository.filtraAnnunci(
                null, "Matematica Discreta", 20))
                .thenReturn(List.of(annuncio1));

        List<Annuncio> result =
                bachecaService.getAnnunciFiltrati(null, "Matematica Discreta", 20);

        assertEquals(1, result.size());
        assertEquals("Matematica Discreta", result.get(0).getEsame());
    }

    @Test
    void TC7_RicercaFiltriPerEsameCorsoLaurea() {

        when(annuncioRepository.filtraAnnunci(
                "Informatica", "Matematica Discreta", null))
                .thenReturn(List.of(annuncio1));

        List<Annuncio> result =
                bachecaService.getAnnunciFiltrati(
                        "Informatica", "Matematica Discreta", null);

        assertEquals(1, result.size());
    }

    @Test
    void TC8_RicercaFiltriPerTariffaOraria() {

        when(annuncioRepository.filtraAnnunci(
                null, null, 20))
                .thenReturn(List.of(annuncio1, annuncio3));

        List<Annuncio> result =
                bachecaService.getAnnunciFiltrati(null, null, 20);

        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(a -> a.getTariffaOraria() <= 20));
    }

    @Test
    void TC9_RicercaFiltriPerCorsoLaurea() {

        when(annuncioRepository.filtraAnnunci(
                "Informatica", null, null))
                .thenReturn(List.of(annuncio1, annuncio2));

        List<Annuncio> result =
                bachecaService.getAnnunciFiltrati("Informatica", null, null);

        assertEquals(2, result.size());
    }

    @Test
    void TC10_RicercaFiltriPerEsame() {

        when(annuncioRepository.filtraAnnunci(
                null, "Matematica Discreta", null))
                .thenReturn(List.of(annuncio1));

        List<Annuncio> result =
                bachecaService.getAnnunciFiltrati(null, "Matematica Discreta", null);

        assertEquals(1, result.size());
    }

    // ======================
    // CASO 2.11
    // ======================

    @Test
    void TC11_VisualizzaInteraBacheca() {

        when(annuncioRepository.findByDisponibileTrue())
                .thenReturn(List.of(annuncio1, annuncio2, annuncio3));

        List<Annuncio> result = bachecaService.getAnnunciDisponibili();

        assertEquals(3, result.size());
    }
}
