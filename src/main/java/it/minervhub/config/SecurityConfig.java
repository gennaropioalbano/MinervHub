package it.minervhub.config;

import it.minervhub.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    public UserDetailsService userDetailsService(UtenteService utenteService) {
        return email -> {
            var utente =  utenteService.findByEmail(email);
            if (utente == null) throw new UsernameNotFoundException("Utente non trovato");
            return User.builder()
                    .username(utente.getEmail())
                    .password(utente.getPassword())
                    .roles("USER")
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/bacheca", "/bacheca/**", "/register", "/annunci", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customSuccessHandler) // <--- Sostituisci defaultSuccessUrl con questo
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/home") // Consigliato: dove andare dopo il logout
                        .permitAll());
        return http.build();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}