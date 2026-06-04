package com.dieguex.inventaireMaisonBackend.dto;

public record LoginRequestDto(
        String courriel,
        String motPasse
) {
}
