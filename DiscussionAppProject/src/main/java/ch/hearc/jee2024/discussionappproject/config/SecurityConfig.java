package ch.hearc.jee2024.discussionappproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()) // Autorise toutes les requêtes
                .csrf(csrf -> csrf.disable()) // Désactive la protection CSRF
                .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Permet les frames (nécessaire pour H2 Console)

        return http.build();
    }
}
