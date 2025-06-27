// Local: src/main/java/com/example/prova/security/SecurityConfigurations.java
package com.example.prova.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider; // <-- NOVO IMPORT
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // <-- NOVO IMPORT
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    // Injetamos nosso serviço de autorização que criamos
    @Autowired
    private AuthorizationService authorizationService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Primeiro, vamos restaurar a configuração de segurança completa
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
        // Endpoints públicos de autenticação e documentação
        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

        .requestMatchers("/actuator/**").permitAll()

        // Regras específicas para Produtos
        .requestMatchers(HttpMethod.GET, "/produtos/**").authenticated()       // Qualquer um logado (USER ou ADMIN) pode fazer GET
        .requestMatchers(HttpMethod.POST, "/produtos").hasRole("ADMIN")      // Apenas ADMIN pode criar
        .requestMatchers(HttpMethod.PUT, "/produtos/**").hasRole("ADMIN")     // Apenas ADMIN pode atualizar
        .requestMatchers(HttpMethod.DELETE, "/produtos/**").hasRole("ADMIN") // Apenas ADMIN pode deletar

        // Garante que qualquer outra rota não listada precise de login
        .anyRequest().authenticated()
)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // O AuthenticationManager continua o mesmo
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // O PasswordEncoder continua o mesmo
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- A GRANDE MUDANÇA ESTÁ AQUI ---
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Estamos criando nosso próprio provedor de autenticação
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Definimos explicitamente qual é o serviço que busca usuários
        authProvider.setUserDetailsService(authorizationService);
        // Definimos explicitamente qual é o codificador de senhas
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}