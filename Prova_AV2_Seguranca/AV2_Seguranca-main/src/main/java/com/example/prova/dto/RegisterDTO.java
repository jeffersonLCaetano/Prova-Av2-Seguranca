package com.example.prova.dto;

import com.example.prova.model.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}