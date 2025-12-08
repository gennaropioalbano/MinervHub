package it.minervhub.service;

import it.minervhub.model.Utente;
import it.minervhub.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Tentativo di login con email: " + email);

        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("Utente NON trovato!");
                    return new UsernameNotFoundException("Utente non trovato con email: " + email);
                });

        System.out.println("Utente trovato: " + utente.getEmail());
        System.out.println("Password salvata (criptata): " + utente.getPassword());

        return User.builder()
                .username(utente.getEmail())
                .password(utente.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }
}