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
    private Long idUtente;

    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Column(name = "cognome", length = 100, nullable = false)
    private String cognome;

    @Column(name = "email", length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "dataNascita")
    private LocalDate dataNascita;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "corsoLaurea", length = 50)
    private String corsoLaurea;

    public Utente(LocalDate dataNascita, String nome, String cognome, String email, String password, String corsoLaurea) {
        this.dataNascita = dataNascita;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
        this.corsoLaurea = corsoLaurea;
    }

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorsoLaurea() {
        return corsoLaurea;
    }

    public void setCorsoLaurea(String corsoLaurea) {
        this.corsoLaurea = corsoLaurea;
    }
}
