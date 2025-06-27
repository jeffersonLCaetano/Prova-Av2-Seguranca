package com.example.prova.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.prova.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
}
