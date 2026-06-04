package com.dieguex.inventaireMaisonBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "Le courriel est obligatoire")
        @Email(message = "Le format du courriel est invalide")
        String courriel,
        @NotBlank(message = "Le mot de passe est obligatoire")
        String motPasse
) {
}
