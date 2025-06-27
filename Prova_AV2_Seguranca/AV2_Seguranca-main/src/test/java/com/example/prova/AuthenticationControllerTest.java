package com.example.prova;

import com.example.prova.dto.AuthenticationDTO;
import com.example.prova.dto.RegisterDTO;
import com.example.prova.model.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest // Indica que este é um teste de integração que sobe o contexto do Spring
@AutoConfigureMockMvc // Configura automaticamente o MockMvc para fazermos requisições HTTP
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc; // Objeto que simula as requisições para a nossa API

    @Autowired
    private ObjectMapper objectMapper; // Utilitário para converter objetos Java em JSON

    @Test
    void register_DeveRetornarStatus200_QuandoUsuarioValido() throws Exception {
        // 1. Prepara os dados do novo usuário (nosso DTO)
        var registerDto = new RegisterDTO("teste.junit@email.com", "senha123", UserRole.USER);

        // 2. Converte o objeto DTO para uma string JSON
        String jsonBody = objectMapper.writeValueAsString(registerDto);

        // 3. Executa a requisição e verifica o resultado
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register") // Faz um POST para /auth/register
                        .contentType(MediaType.APPLICATION_JSON) // Define o tipo de conteúdo como JSON
                        .content(jsonBody)) // Adiciona o corpo JSON à requisição
                .andExpect(MockMvcResultMatchers.status().isOk()); // Espera que o status da resposta seja 200 (OK)
    }


    @Test
    void login_DeveRetornarToken_QuandoCredenciaisValidas() throws Exception {
        // ARRANGE (Arrumar): Primeiro, garantimos que o usuário existe, registrando-o.
        // Usamos um email diferente para não conflitar com o outro teste.
        var registerDto = new RegisterDTO("login.test@email.com", "senhaForte", UserRole.USER);
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Agora, preparamos os dados para o login
        var authDto = new AuthenticationDTO("login.test@email.com", "senhaForte");
        String jsonBody = objectMapper.writeValueAsString(authDto);

        // ACT & ASSERT (Agir e Verificar)
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(MockMvcResultMatchers.status().isOk()) // Espera status 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists()) // Verifica se o campo "token" existe no JSON
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty()); // Verifica se o valor do token não é vazio
    }

}