// Local: src/main/java/com/example/prova/security/SecurityFilter.java
package com.example.prova.security;

import com.example.prova.repository.UsuarioRepository; // Verifique se o import está correto
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Anotação que indica que esta é uma classe gerenciada pelo Spring
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Recupera o token do cabeçalho da requisição
        var token = this.recuperarToken(request);

        // 2. Se houver um token, valida-o
        if (token != null) {
            // 3. Pega o login (subject) de dentro do token
            var login = tokenService.validarToken(token);
            // 4. Busca o usuário no banco de dados com base no login extraído do token
            UserDetails usuario = usuarioRepository.findByLogin(login);

            // 5. Se o usuário for encontrado, cria um objeto de autenticação
            // e o coloca no contexto de segurança do Spring
            if (usuario != null) {
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 6. Continua a cadeia de filtros, passando para o próximo filtro
        filterChain.doFilter(request, response);
    }

    /**
     * Método auxiliar para extrair o token do cabeçalho "Authorization".
     */
    private String recuperarToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        // O token vem no formato "Bearer <token>", então removemos o prefixo "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}