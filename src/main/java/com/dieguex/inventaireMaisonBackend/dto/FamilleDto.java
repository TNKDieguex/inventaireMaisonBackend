package com.dieguex.inventaireMaisonBackend.dto;

import com.dieguex.inventaireMaisonBackend.model.Famille;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record FamilleDto(
        @NotBlank(message = "Le nom de la famille est obligatoire")
        String nomFamille,
        List<UtilisateurDto> utilisateurs,
        UUID uuid
) {
    public static FamilleDto versDto(Famille famille){
        return new FamilleDto(
                famille.getNomFamille(),
                famille.getListeUtilisateurs().stream().map(UtilisateurDto::versDto).toList(),
                famille.getUuid()
        );
    }
    public static Famille versEntite(FamilleDto familleDto){
        return new Famille.Builder()
                .setNomFamille(familleDto.nomFamille())
                .build();
    }
}
