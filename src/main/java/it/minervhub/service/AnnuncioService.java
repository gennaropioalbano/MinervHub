package it.minervhub.service;

import it.minervhub.exceptions.AnnuncioException;
import it.minervhub.model.Annuncio;
import it.minervhub.model.AnnuncioDTO;
import it.minervhub.model.Utente;
import it.minervhub.repository.AnnuncioRepository;
import it.minervhub.repository.UtenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service che gestisce la logica di business relativa agli annunci.
 * Si occupa della creazione, modifica, eliminazione e recupero
 * degli annunci, oltre alla conversione tra Entity e DTO.
 */
@Service
public class AnnuncioService {

    /** Repository per la gestione degli annunci */
    @Autowired
    private AnnuncioRepository annuncioRepository;

    /** Repository per la gestione degli utenti */
    @Autowired
    private UtenteRepository utenteRepository;

    /**
     * Recupera tutti gli annunci ordinati per ID in ordine decrescente.
     *
     * @return lista di annunci
     */
    public List<Annuncio> getAnnunciDisponibili() {
        return annuncioRepository.findByDisponibileTrue();
    }

    /**
     * Recupera un annuncio tramite il suo identificativo.
     *
     * @param id identificativo dell'annuncio
     * @return annuncio trovato oppure null se non esiste
     */
    public Annuncio getAnnuncioById(Long id) {
        Optional<Annuncio> annuncio = annuncioRepository.findById(id);
        return annuncio.orElse(null);
    }

    /**
     * Recupera tutti gli annunci creati da un utente identificato tramite email.
     *
     * @param email email dell'autore
     * @return lista di annunci dell'utente oppure lista vuota se non trovato
     */
    public List<Annuncio> findByAutoreEmailAndDisponibile(String email, boolean disponibile) {
        Utente utente = utenteRepository.findByEmail(email);
        if (utente == null) {
            return List.of(); // lista vuota se l'utente non esiste
        }
        return annuncioRepository.findByAutoreAndDisponibile(utente, disponibile);
    }

    /**
     * Crea un nuovo annuncio associandolo all'utente autenticato.
     *
     * @param dto DTO contenente i dati dell'annuncio
     * @param email email dell'utente autore
     */
    public void creaAnnuncio(AnnuncioDTO dto, String email) {

        validaAnnuncio(dto);

        Utente utente =  utenteRepository.findByEmail(email);
        if (utente == null) throw new IllegalArgumentException("Utente non trovato");

        Annuncio annuncio = new Annuncio();
        annuncio.setAutore(utente);
        annuncio.setTitolo(dto.getTitolo());
        annuncio.setDescrizione(dto.getDescrizione());
        annuncio.setEsame(dto.getEsame());
        annuncio.setCorsoLaurea(dto.getCorsoLaurea());
        annuncio.setTariffaOraria(dto.getTariffaOraria());
        annuncio.setDisponibile(true);

        // Conversione stringa scambio -> lista
        annuncio.setScambio(
                dto.getScambio() == null || dto.getScambio().isBlank()
                    ? Collections.emptyList()
                    : Arrays.stream(dto.getScambio().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .toList()
        );

        annuncioRepository.save(annuncio);
    }

    /**
     * Modifica un annuncio esistente se l'utente è autorizzato.
     *
     * @param id identificativo dell'annuncio
     * @param dto DTO contenente i dati aggiornati
     * @param email email dell'utente autenticato
     * @throws RuntimeException se l'annuncio non esiste o l'utente non è autorizzato
     */
    public void modificaAnnuncio(Long id, AnnuncioDTO dto, String email) {

        validaAnnuncio(dto);

        Annuncio annuncio = annuncioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annuncio non trovato"));

        if (!annuncio.getAutore().getEmail().equals(email)) {
            throw new RuntimeException("Utente non autorizzato");
        }

        annuncio.setTitolo(dto.getTitolo());
        annuncio.setDescrizione(dto.getDescrizione());
        annuncio.setEsame(dto.getEsame());
        annuncio.setCorsoLaurea(dto.getCorsoLaurea());
        annuncio.setTariffaOraria(dto.getTariffaOraria());

        // Conversione stringa scambio -> lista
        annuncio.setScambio(
                dto.getScambio() == null || dto.getScambio().isBlank()
                        ? Collections.emptyList()
                        : Arrays.stream(dto.getScambio().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isBlank())
                        .toList()
        );

        annuncioRepository.save(annuncio);
    }

    /**
     * Disattiva (soft delete) un annuncio rendendolo non disponibile.
     *
     * @param id identificativo dell'annuncio
     */
    @Transactional
    public void eliminaAnnuncio(Long id) {
        Annuncio annuncio = annuncioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Annuncio non trovato"));

        annuncio.setDisponibile(false);

        annuncioRepository.save(annuncio);
    }

    // ---------------- METODI DI UTILITÀ ----------------

    /**
     * Converte un'entità Annuncio in AnnuncioDto.
     * Utilizzato per pre-popolare il form di modifica.
     *
     * @param annuncio entità annuncio
     * @return DTO popolato
     */
    public AnnuncioDTO mapEntityToDto(Annuncio annuncio) {
        AnnuncioDTO dto = new AnnuncioDTO();
        dto.setId(annuncio.getId());
        dto.setTitolo(annuncio.getTitolo());
        dto.setDescrizione(annuncio.getDescrizione());
        dto.setEsame(annuncio.getEsame());
        dto.setCorsoLaurea(annuncio.getCorsoLaurea());
        dto.setTariffaOraria(annuncio.getTariffaOraria());

        // Converte la lista di stringhe in una stringa unica separata da virgole
        if (annuncio.getScambio() != null && !annuncio.getScambio().isEmpty()) {
            dto.setScambio(String.join(", ", annuncio.getScambio()));
        }
        return dto;
    }

    /**
     * Converte un DTO in entità Annuncio.
     * Metodo di supporto comune per creazione e modifica.
     *
     * @param dto DTO contenente i dati
     * @param annuncio entità da aggiornare
     */
    private void mapDtoToEntity(AnnuncioDTO dto, Annuncio annuncio) {
        annuncio.setTitolo(dto.getTitolo());
        annuncio.setDescrizione(dto.getDescrizione());
        annuncio.setEsame(dto.getEsame());
        annuncio.setCorsoLaurea(dto.getCorsoLaurea());
        annuncio.setTariffaOraria(dto.getTariffaOraria());

        // Logica split stringa -> lista
        if (dto.getScambio() != null && !dto.getScambio().isBlank()) {
            List<String> listaScambi = Arrays.stream(dto.getScambio().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList();
            annuncio.setScambio(listaScambi);
        } else {
            annuncio.setScambio(null);
        }
    }

    private void validaAnnuncio(AnnuncioDTO dto) {
        if (dto.getTitolo() == null || dto.getTitolo().isBlank() || dto.getTitolo().length() > 50)
            throw new AnnuncioException("Titolo non valido");
        if (dto.getDescrizione() == null || dto.getDescrizione().isBlank() || dto.getDescrizione().length() > 150)
            throw new AnnuncioException("Descrizione non valida");
        if (dto.getEsame() == null || dto.getEsame().isBlank() || dto.getEsame().length() > 50)
            throw new AnnuncioException("Esame non valido");
        if (dto.getCorsoLaurea() == null || dto.getCorsoLaurea().isBlank() || dto.getCorsoLaurea().length() > 50)
            throw new AnnuncioException("Corso di laurea non valido");
        if (dto.getTariffaOraria() < 5 || dto.getTariffaOraria() > 50)
            throw new AnnuncioException("Tariffa non valida");
        if (dto.getScambio() != null && dto.getScambio().length() > 150)
            throw new AnnuncioException("Scambio troppo lungo");
    }
}