package com.dieguex.inventaireMaisonBackend.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreationFamilleDto(
        @NotBlank(message = "Le nom de la famille est obligatoire")
        String nomFamille
) {
}
