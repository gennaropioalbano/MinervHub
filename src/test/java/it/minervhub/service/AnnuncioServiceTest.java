package it.minervhub.service;

import it.minervhub.model.Annuncio;
import it.minervhub.model.AnnuncioDto;
import it.minervhub.model.Utente;
import it.minervhub.repository.AnnuncioRepository;
import it.minervhub.repository.UtenteRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnuncioServiceTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void titoloVuoto_shouldFailValidation() {
        AnnuncioDto dto = new AnnuncioDto();
        dto.setTitolo(""); // titolo vuoto
        dto.setDescrizione("Una descrizione valida");
        dto.setEsame("Matematica");
        dto.setCorsoLaurea("Ingegneria");
        dto.setTariffaOraria(10);

        var violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("obbligatorio")));
    }

    @Test
    void titoloTroppoLungo_shouldFailValidation() {
        AnnuncioDto dto = new AnnuncioDto();
        dto.setTitolo("T".repeat(100)); // titolo troppo lungo
        dto.setDescrizione("Una descrizione valida");
        dto.setEsame("Matematica");
        dto.setCorsoLaurea("Ingegneria");
        dto.setTariffaOraria(10);

        var violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("troppo lungo")));
    }
}