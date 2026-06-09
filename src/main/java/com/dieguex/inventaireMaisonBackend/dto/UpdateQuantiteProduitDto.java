package com.dieguex.inventaireMaisonBackend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateQuantiteProduitDto(
        @Min(value = 0, message = "La quantité ne peut pas être négative")
        @NotNull(message = "La quantité ne peut pas être nulle")
        int quantite
) {
}
