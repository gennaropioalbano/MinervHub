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

@Service
public class AnnuncioService {

    @Autowired
    private AnnuncioRepository annuncioRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    // Recupera tutti gli annunci ordinati per ID decrescente
    public List<Annuncio> findAll() {
        return annuncioRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    // Recupera un singolo annuncio (ritorna Optional per gestire null safe)
    public Optional<Annuncio> findById(Long id) {
        return annuncioRepository.findById(id);
    }

    // Recupera gli annunci di uno specifico utente (tramite email)
    public List<Annuncio> findByAutoreEmail(String email) {
        Utente utente = utenteRepository.findByEmail(email);
        if (utente == null) {
            return List.of(); // Ritorna lista vuota se utente non trovato
        }
        return annuncioRepository.findByAutore(utente);
    }

    public void save(AnnuncioDto dto, String email) {
        Utente utente =  utenteRepository.findByEmail(email);

        Annuncio annuncio = new Annuncio();
        annuncio.setAutore(utente);
        annuncio.setTitolo(dto.getTitolo());
        annuncio.setDescrizione(dto.getDescrizione());
        annuncio.setEsame(dto.getEsame());
        annuncio.setCorsoLaurea(dto.getCorsoLaurea());
        annuncio.setTariffaOraria(dto.getTariffaOraria());
        annuncio.setDisponibile(true);
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

    // MODIFICA ANNUNCIO: Aggiorna solo se esiste
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

    // CANCELLAZIONE
    public void eliminaAnnuncio(Long id) {
        if (annuncioRepository.existsById(id)) {
            annuncioRepository.deleteById(id);
        }
    }

    // --- METODI DI UTILITÀ (HELPER) ---

    // Converte l'Entità in DTO (utile per pre-popolare il form di Modifica)
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

    // Converte il DTO in Entità (logica comune per Create e Edit)
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