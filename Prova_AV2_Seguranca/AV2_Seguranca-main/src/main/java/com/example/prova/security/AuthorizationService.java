// Local: src/main/java/com/example/prova/security/AuthorizationService.java
package com.example.prova.security;

import com.example.prova.repository.UsuarioRepository; // Verifique o import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    // Este método é chamado pelo Spring Security quando um usuário tenta se autenticar.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // A nossa lógica para encontrar o usuário no banco de dados.
        // O `username` que recebemos aqui é o `login` do nosso DTO.
        UserDetails user = repository.findByLogin(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        return user;
    }
}