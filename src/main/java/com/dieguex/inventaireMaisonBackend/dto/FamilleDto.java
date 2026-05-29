package com.dieguex.inventaireMaisonBackend.dto;

import com.dieguex.inventaireMaisonBackend.model.Famille;

import java.util.UUID;

public record FamilleDto(
        String nomFamille,
        UUID uuid
) {
    public static FamilleDto versDto(Famille famille){
        return new FamilleDto(
                famille.getNomFamille(),
                famille.getUuid()
        );
    }
    public static Famille versEntite(FamilleDto familleDto){
        return new Famille.Builder()
                .setNomFamille(familleDto.nomFamille())
                .build();
    }
}
