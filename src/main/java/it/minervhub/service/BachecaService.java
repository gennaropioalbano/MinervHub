package it.minervhub.service;

import it.minervhub.exceptions.InvalidFiltroException;
import it.minervhub.model.Annuncio;
import it.minervhub.repository.AnnuncioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service responsabile della gestione della bacheca degli annunci.
 * <p>
 * Contiene la logica di business relativa alla consultazione degli annunci
 * e applica le regole di validazione sui criteri di filtro prima di
 * delegare l’accesso ai dati al repository.
 */
@Service
public class BachecaService {

    private final AnnuncioRepository annuncioRepository;

    /**
     * Costruttore con injection del repository degli annunci.
     *
     * @param annuncioRepository repository per l’accesso ai dati degli annunci
     */
    public BachecaService(AnnuncioRepository annuncioRepository) {
        this.annuncioRepository = annuncioRepository;
    }

    /**
     * Restituisce la lista di tutti gli annunci attualmente disponibili.
     *
     * @return lista di annunci disponibili
     */
    public List<Annuncio> getAnnunciDisponibili() {
        return annuncioRepository.findByDisponibileTrue();
    }

    /**
     * Restituisce la lista di annunci filtrati in base ai criteri forniti.
     * <p>
     * Prima di eseguire la ricerca, il metodo valida i parametri di input
     * applicando i vincoli di dominio. In caso di parametri non validi,
     * viene sollevata un’eccezione.
     *
     * @param corsoLaurea corso di laurea su cui filtrare (opzionale)
     * @param esame nome dell’esame su cui filtrare (opzionale)
     * @param tariffaMax tariffa oraria massima consentita (opzionale)
     * @return lista di annunci che soddisfano i criteri di filtro
     * @throws InvalidFiltroException se uno o più parametri non rispettano i vincoli previsti
     */
    public List<Annuncio> getAnnunciFiltrati(String corsoLaurea, String esame, Integer tariffaMax) {

        if (corsoLaurea != null && corsoLaurea.length() > 50) {
            throw new InvalidFiltroException("Il corso di laurea non può superare i 50 caratteri.");
        }

        if (esame != null && esame.length() > 50) {
            throw new InvalidFiltroException("Il nome dell’esame non può superare i 50 caratteri.");
        }

        if (tariffaMax != null && (tariffaMax < 5 || tariffaMax > 50)) {
            throw new InvalidFiltroException("La tariffa oraria deve essere compresa tra 5 e 50 euro.");
        }

        return annuncioRepository.filtraAnnunci(corsoLaurea, esame, tariffaMax);
    }

    /**
     * Recupera un annuncio a partire dal suo identificativo.
     *
     * @param id identificativo dell’annuncio
     * @return l’annuncio se presente, {@code null} altrimenti
     */
    public Annuncio getAnnuncioById(Long id) {
        Optional<Annuncio> annuncio = annuncioRepository.findById(id);
        return annuncio.orElse(null);
    }
}
