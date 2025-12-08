package it.minervhub.config; // O il tuo package

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // 1. Pagine accessibili a TUTTI (senza login)
                        .requestMatchers("/", "/home", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                        // 2. Tutte le altre pagine richiedono il LOGIN
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login") // La nostra pagina custom
                        .defaultSuccessUrl("/home", true) // Dove vai se il login riesce
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Dove vai dopo il logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}