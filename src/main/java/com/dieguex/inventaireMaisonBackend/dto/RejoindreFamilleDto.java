package com.dieguex.inventaireMaisonBackend.dto;

import jakarta.validation.constraints.NotNull;

public record RejoindreFamilleDto(
        @NotNull java.util.UUID familleUuid
) {
}
