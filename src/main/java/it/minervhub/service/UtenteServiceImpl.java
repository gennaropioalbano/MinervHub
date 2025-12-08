package it.minervhub.service;

import it.minervhub.model.Utente;
import it.minervhub.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtenteServiceImpl implements UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Utente createUser(Utente utente) {
        try {
            System.out.println("=== SERVICE: INIZIO createUser ===");
            System.out.println("Utente ricevuto: " + utente);
            System.out.println("Email: " + utente.getEmail());
            System.out.println("Password in chiaro: " + utente.getPassword());
            System.out.println("Nome: " + utente.getNome());
            System.out.println("Cognome: " + utente.getCognome());
            System.out.println("Corso: " + utente.getCorsoLaurea());

            // Cripta la password prima di salvare
            String passwordCriptata = passwordEncoder.encode(utente.getPassword());
            utente.setPassword(passwordCriptata);

            System.out.println("Password criptata: " + passwordCriptata);

            System.out.println("=== Chiamata a repository.save() ===");
            Utente saved = utenteRepository.save(utente);

            System.out.println("=== Utente salvato! ===");
            System.out.println("ID assegnato: " + saved.getIdUtente());
            System.out.println("Email salvata: " + saved.getEmail());

            return saved;
        } catch (Exception e) {
            System.out.println("=== ERRORE NEL SERVICE ===");
            e.printStackTrace();
            throw e;
        }
    }
}