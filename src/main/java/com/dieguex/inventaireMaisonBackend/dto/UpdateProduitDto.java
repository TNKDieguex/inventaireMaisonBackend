package com.dieguex.inventaireMaisonBackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateProduitDto(
        @NotNull(message = "La quantité ne peut pas être nulle")
        int quantite,
        @NotNull(message = "La quantité minimale ne peut pas être nulle")
        @Min(value = 0, message = "La quantité minimale doit être supérieure ou égale à zéro")
        double quantiteMinimal,
        @NotNull(message = "La date limite de consommation ne peut pas être nulle")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateLimiteConsommation,
        @NotNull(message = "La note ne peut pas être nulle")
        String notes,
        @NotNull(message = "L'uuid ne peut pas être nulle")
        UUID uuid
) {
}
