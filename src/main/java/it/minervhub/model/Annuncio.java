package it.minervhub.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "annuncio")
public class Annuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titolo", length = 50, nullable = false)
    private String titolo;

    @Column(name = "descrizione", length = 150, nullable = false)
    private String descrizione;

    @Column(name = "disponibile", nullable = false)
    private boolean disponibile;

    @Column(name = "esame", length = 30, nullable = false)
    private String esame;

    @Column(name = "tariffaOraria", nullable = false)
    private Integer tariffaOraria;

    @Column(name = "scambioLezioni", length = 150)
    private String scambioLezioni;

    @Column(name = "corsoLaurea", length = 30)
    private String corsoLaurea;

    @ManyToOne
    @JoinColumn(name = "autore", nullable = false)
    private Utente autore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean isDisponibile() {
        return disponibile;
    }

    public void setDisponibile(boolean disponibile) {
        this.disponibile = disponibile;
    }

    public String getEsame() {
        return esame;
    }

    public void setEsame(String esame) {
        this.esame = esame;
    }

    public Integer getTariffaOraria() {
        return tariffaOraria;
    }

    public void setTariffaOraria(Integer tariffaOraria) {
        this.tariffaOraria = tariffaOraria;
    }

    public String getScambioLezioni() {
        return scambioLezioni;
    }

    public void setScambioLezioni(String scambioLezioni) {
        this.scambioLezioni = scambioLezioni;
    }

    public String getCorsoLaurea() {
        return corsoLaurea;
    }

    public void setCorsoLaurea(String corsoLaurea) {
        this.corsoLaurea = corsoLaurea;
    }

    public Utente getAutore() {
        return autore;
    }

    public void setAutore(Utente autore) {
        this.autore = autore;
    }
}