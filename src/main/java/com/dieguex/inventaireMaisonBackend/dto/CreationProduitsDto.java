package com.dieguex.inventaireMaisonBackend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreationProduitsDto(
        @NotNull(message = "La liste des produits ne peut pas être nulle")
        @Valid
        List<ProduitDto> produitDtoList,
        @NotNull(message = "L'UUID de la famille est obligatoire")
        UUID familleUuid
) {
}
