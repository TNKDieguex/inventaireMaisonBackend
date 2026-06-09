package com.dieguex.inventaireMaisonBackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateNoteProduitDto(
        @NotNull(message = "Les notes ne peuvent pas être nulles")
        @Size(max = 100, message = "Les notes ne peuvent pas dépasser 100 caractères")
        String notes
) {
}