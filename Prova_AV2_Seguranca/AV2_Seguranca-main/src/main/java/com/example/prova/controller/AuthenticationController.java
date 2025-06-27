// Local: src/main/java/com/example/prova/controller/AuthenticationController.java
package com.example.prova.controller;

import com.example.prova.dto.AuthenticationDTO;
import com.example.prova.dto.LoginResponseDTO;
import com.example.prova.dto.RegisterDTO;
import com.example.prova.model.Usuario;
import com.example.prova.repository.UsuarioRepository;
import com.example.prova.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationDTO data){
        // Cria um objeto de autenticação com o login e a senha recebidos
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        
        // O AuthenticationManager vai usar nosso serviço para autenticar o usuário
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Se a autenticação for bem-sucedida, gera um token para o usuário
        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        // Retorna o token em um DTO de resposta
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data){
        // Verifica se o login já existe no banco
        if(this.repository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        // Criptografa a senha antes de salvar
        String encryptedPassword = passwordEncoder.encode(data.password());
        
        // Cria uma nova instância da entidade Usuario
        Usuario newUser = new Usuario(null, data.login(), encryptedPassword, data.role());

        // Salva o novo usuário no banco de dados
        this.repository.save(newUser);

        // Retorna uma resposta de sucesso (200 OK)
        return ResponseEntity.ok().build();
    }
}
