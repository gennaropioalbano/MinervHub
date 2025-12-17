package it.minervhub.controller;

import it.minervhub.model.Annuncio;
import it.minervhub.model.AnnuncioDto;
import it.minervhub.model.Utente;
import it.minervhub.repository.AnnuncioRepository;
import it.minervhub.repository.UtenteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/annunci")
public class AnnuncioController {

    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private AnnuncioRepository annuncioRepository;

    @GetMapping({"", "/"})
    public String showAnnuncioList(Model model) {
        List<Annuncio> annunci = annuncioRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("annunci", annunci);
        return "bacheca";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        AnnuncioDto annuncioDto = new AnnuncioDto();
        model.addAttribute("annuncioDto", annuncioDto);
        return "createAnnuncio";
    }

    @PostMapping("/create")
    public String createAnnuncio(
                  @Valid @ModelAttribute("annuncioDto") AnnuncioDto dto,
                  BindingResult result,
                  Model model) {
        if (result.hasErrors()) {
            return "createAnnuncio";
        }

        Annuncio annuncio = new Annuncio();
        annuncio.setTitolo(dto.getTitolo());
        annuncio.setDescrizione(dto.getDescrizione());
        annuncio.setEsame(dto.getEsame());
        annuncio.setCorsoLaurea(dto.getCorsoLaurea());
        annuncio.setTariffaOraria(dto.getTariffaOraria());
        annuncio.setDisponibile(true);

        if (dto.getScambio() != null && !dto.getScambio().isBlank()) {
            annuncio.setScambio(
                    Arrays.stream(dto.getScambio().split(","))
                            .map(String::trim)
                            .toList()
            );
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Utente autore = utenteRepository.findByEmail(username);

        annuncio.setAutore(autore);

        annuncioRepository.save(annuncio);

        return "redirect:/annunci";
    }

    @GetMapping("/delete/{id}")
    public String deleteAnnuncio(@PathVariable Long id) {
        annuncioRepository.findById(id).ifPresent(annuncioRepository::delete);
        return "redirect:/annunci";
    }

    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        Annuncio annuncio = annuncioRepository.findById(id).orElse(null);
        if (annuncio == null) {
            return "redirect:/annunci";
        }

        AnnuncioDto dto = new AnnuncioDto();
        dto.setTitolo(annuncio.getTitolo());
        dto.setDescrizione(annuncio.getDescrizione());
        dto.setEsame(annuncio.getEsame());
        dto.setCorsoLaurea(annuncio.getCorsoLaurea());
        dto.setTariffaOraria(annuncio.getTariffaOraria());

        if (annuncio.getScambio() != null && !annuncio.getScambio().isEmpty()) {
            dto.setScambio(String.join(",", annuncio.getScambio()));
        }

        model.addAttribute("annuncioDto", dto);
        model.addAttribute("annuncioId", id);

        return "editAnnuncio";
    }

    @PostMapping("/edit/{id}")
    public String editAnnuncio(
            @PathVariable Long id,
            @Valid @ModelAttribute("annuncioDto") AnnuncioDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("annuncioId", id);
            return "editAnnuncio";
        }

        Annuncio annuncio = annuncioRepository.findById(id).orElse(null);
        if (annuncio == null) {
            return "redirect:/annunci";
        }

        annuncio.setTitolo(dto.getTitolo());
        annuncio.setDescrizione(dto.getDescrizione());
        annuncio.setEsame(dto.getEsame());
        annuncio.setCorsoLaurea(dto.getCorsoLaurea());
        annuncio.setTariffaOraria(dto.getTariffaOraria());

        if (dto.getScambio() != null && !dto.getScambio().isBlank()) {
            annuncio.setScambio(Arrays.stream(dto.getScambio().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList());
        } else {
            annuncio.setScambio(null); // se vuoto, niente scambio
        }

        annuncioRepository.save(annuncio);

        return "redirect:/annunci";
    }

    @GetMapping("/miei")
    public String showMyAnnunci(Model model) {
        // 1. Recupera l'utente loggato
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Utente utenteLoggato = utenteRepository.findByEmail(email);

        // 2. Trova solo gli annunci di questo utente
        List<Annuncio> mieiAnnunci = annuncioRepository.findByAutore(utenteLoggato);

        // 3. Aggiungi al modello
        model.addAttribute("annunci", mieiAnnunci);

        // 4. Ritorna la vista specifica (o riusiamo bacheca con un flag)
        return "mieiAnnunci";
    }

    @GetMapping("/detail/{id}")
    public String showAnnuncioDetail(@PathVariable Long id, Model model) {
        // 1. Cerca l'annuncio nel DB
        Annuncio annuncio = annuncioRepository.findById(id).orElse(null);

        // 2. Se non esiste, torna alla lista principale
        if (annuncio == null) {
            return "redirect:/annunci";
        }

        // 3. Aggiungi l'oggetto al model
        model.addAttribute("annuncio", annuncio);

        // 4. Opzionale: Passa anche l'utente loggato per mostrare/nascondere bottoni "Modifica"
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        model.addAttribute("currentUsername", currentUsername);

        return "annuncio";
    }
}
