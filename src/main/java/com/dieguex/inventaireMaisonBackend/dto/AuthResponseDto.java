package com.dieguex.inventaireMaisonBackend.dto;

public record AuthResponseDto(
        String token,
        UtilisateurDto utilisateur
) {
}
