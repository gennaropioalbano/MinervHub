package it.minervhub.service;

import it.minervhub.model.Utente;
import it.minervhub.repository.UtenteRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService implements UserDetailsService {

    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // QUESTO METODO SERVE PER IL LOGIN
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔍 TENTATIVO LOGIN: Cerco utente con email: " + email);

        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        System.out.println("✅ UTENTE TROVATO NEL DB: " + utente.getEmail());
        System.out.println("🔑 PASSWORD NEL DB (Deve essere lunga e strana): " + utente.getPassword());

        return User.builder()
                .username(utente.getEmail())
                .password(utente.getPassword()) // Passiamo la password criptata a Spring Security
                .roles("USER")
                .build();
    }

    // QUESTO METODO SERVE PER LA REGISTRAZIONE
    public void salvaUtente(Utente utente) {
        System.out.println("📝 REGISTRAZIONE: Password originale inserita: " + utente.getPassword());

        // Criptiamo la password
        String passwordCriptata = passwordEncoder.encode(utente.getPassword());
        utente.setPassword(passwordCriptata);

        System.out.println("🔒 REGISTRAZIONE: Password criptata che sto salvando: " + passwordCriptata);

        utenteRepository.save(utente);
    }
}