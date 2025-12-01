package it.minervhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "utente")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int idUtente;

    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Column(name = "cognome", length = 100, nullable = false)
    private String cognome;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "dataNascita")
    private LocalDate dataNascita;

    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @Column(name = "corsoLaurea", length = 50)
    private String corsoLaurea;
}
