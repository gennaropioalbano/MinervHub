package it.minervhub.model;

import it.minervhub.config.StringListConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "annuncio")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Annuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "titolo", length = 50, nullable = false)
    private String titolo;

    @Column(name = "descrizione", length = 150, nullable = false)
    private String descrizione;

    @Column(name = "disponibile", nullable = false)
    private boolean disponibile;

    @Column(name = "esame", length = 30, nullable = false)
    private String esame;

    @Column(name = "tariffa_oraria", nullable = false)
    private Integer tariffaOraria;

    @Column(name = "scambio", length = 255)
    @Convert(converter = StringListConverter.class)
    private List<String> scambio;

    @Column(name = "corso_laurea", length = 30)
    private String corsoLaurea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autore_id", nullable = false)
    private Utente autore;

    public Annuncio() {
    }

    public Annuncio(String titolo, String descrizione, boolean disponibile, String esame, Integer tariffaOraria, List<String> scambio, String corsoLaurea, Utente autore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.disponibile = disponibile;
        this.esame = esame;
        this.tariffaOraria = tariffaOraria;
        this.scambio = scambio;
        this.corsoLaurea = corsoLaurea;
        this.autore = autore;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<String> getScambio() {
        return scambio;
    }

    public void setScambio(List<String> scambio) {
        this.scambio = scambio;
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