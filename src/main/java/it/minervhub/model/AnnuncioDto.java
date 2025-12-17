package it.minervhub.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
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
}
