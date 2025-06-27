// Local: src/main/java/com/example/prova/security/TokenService.java
package com.example.prova.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.prova.model.Usuario; // Verifique se o import para sua entidade Usuario está correto
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    // Injeta o segredo do nosso arquivo application.yml
    @Value("${api.security.jwt.secret}")
    private String secret;

    /**
     * Gera um token JWT para o usuário fornecido.
     */
    public String gerarToken(Usuario usuario) {
        try {
            // Define o algoritmo de assinatura usando nosso segredo
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("auth-api") // Identifica quem está emitindo o token
                    .withSubject(usuario.getLogin()) // O "dono" do token, neste caso, o login do usuário
                    .withExpiresAt(gerarDataExpiracao()) // Define a data de expiração do token
                    .sign(algoritmo); // Assina o token com o algoritmo
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    /**
     * Valida o token e retorna o login do usuário se o token for válido.
     */
    public String validarToken(String token){
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.require(algoritmo)
                    .withIssuer("auth-api") // Verifica se o emissor é o mesmo
                    .build()
                    .verify(token) // Verifica a assinatura e a validade do token
                    .getSubject(); // Pega o "dono" (subject) do token
        } catch (JWTVerificationException exception){
            // Se o token for inválido (expirado, assinatura errada, etc.), retorna uma string vazia.
            return "";
        }
    }

    /**
     * Gera a data de expiração do token (ex: 2 horas a partir de agora).
     */
    private Instant gerarDataExpiracao() {
        // Token expira em 2 horas, no fuso horário de Brasília (-3)
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}