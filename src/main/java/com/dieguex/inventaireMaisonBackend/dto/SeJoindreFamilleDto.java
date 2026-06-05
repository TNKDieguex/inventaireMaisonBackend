package com.dieguex.inventaireMaisonBackend.dto;

import java.util.UUID;

public record SeJoindreFamilleDto(
        UUID utilisateurUuid,
        UUID familleUuid
) {
}
