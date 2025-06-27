package com.example.prova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.prova.model.Usuario;

public interface  UsuarioRepository extends JpaRepository<Usuario,String> {

    UserDetails findByLogin(String login);
    
}
