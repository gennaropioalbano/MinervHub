package it.minervhub.service;

import it.minervhub.model.Annuncio;
import it.minervhub.model.AnnuncioDto;
import it.minervhub.model.Utente;
import it.minervhub.repository.AnnuncioRepository;
import it.minervhub.repository.UtenteRepository;
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
    public List<Annuncio> findAll() {
        return annuncioRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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
    public List<Annuncio> findByAutoreEmail(String email) {
        Utente utente = utenteRepository.findByEmail(email);
        if (utente == null) {
            return List.of(); // Ritorna lista vuota se utente non trovato
        }
        return annuncioRepository.findByAutore(utente);
    }

    /**
     * Crea un nuovo annuncio associandolo all'utente autenticato.
     *
     * @param dto DTO contenente i dati dell'annuncio
     * @param email email dell'utente autore
     */
    public void modificaAnnuncio(AnnuncioDto dto, String email) {
        Utente utente =  utenteRepository.findByEmail(email);

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
    public void modificaAnnuncio(Long id, AnnuncioDto dto, String email) {

        Annuncio annuncio = annuncioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Annuncio non trovato"));

        if (!annuncio.getAutore().getEmail().equals(email)) {
            throw new RuntimeException("Non autorizzato");
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
     * Aggiorna un annuncio esistente se presente nel database.
     *
     * @param id identificativo dell'annuncio
     * @param dto DTO contenente i dati aggiornati
     * @return true se l'annuncio è stato aggiornato, false se non esiste
     */
    public boolean updateAnnuncio(Long id, AnnuncioDto dto) {
        Optional<Annuncio> annuncioOpt = annuncioRepository.findById(id);

        if (annuncioOpt.isEmpty()) {
            return false; // Annuncio non trovato
        }

        Annuncio annuncio = annuncioOpt.get();

        // Aggiorniamo i campi usando lo stesso helper
        mapDtoToEntity(dto, annuncio);

        // Nota: Non cambiamo l'autore e non cambiamo "disponibile" qui se non richiesto
        annuncioRepository.save(annuncio);
        return true;
    }

    /**
     * Elimina un annuncio se esiste.
     *
     * @param id identificativo dell'annuncio
     */
    public void eliminaAnnuncio(Long id) {
        if (annuncioRepository.existsById(id)) {
            annuncioRepository.deleteById(id);
        }
    }

    // ---------------- METODI DI UTILITÀ ----------------

    /**
     * Converte un'entità Annuncio in AnnuncioDto.
     * Utilizzato per pre-popolare il form di modifica.
     *
     * @param annuncio entità annuncio
     * @return DTO popolato
     */
    public AnnuncioDto mapEntityToDto(Annuncio annuncio) {
        AnnuncioDto dto = new AnnuncioDto();
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
    private void mapDtoToEntity(AnnuncioDto dto, Annuncio annuncio) {
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
}