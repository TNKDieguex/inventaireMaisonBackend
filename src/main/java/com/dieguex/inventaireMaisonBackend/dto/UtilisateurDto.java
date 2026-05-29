package com.dieguex.inventaireMaisonBackend.dto;

import com.dieguex.inventaireMaisonBackend.model.Utilisateur;

import java.util.UUID;

public record UtilisateurDto(
        String nom,
        String courriel,
        String motPasse,
        UUID uuid
) {
    public static UtilisateurDto versDto(Utilisateur utilisateur){
        if (utilisateur == null) return null;
        return new UtilisateurDto(
                utilisateur.getNom(),
                utilisateur.getCourriel(),
                utilisateur.getMotPasse(),
                utilisateur.getUuid()
        );
    }
    public static Utilisateur versEntite(UtilisateurDto utilisateurDto){
        if (utilisateurDto == null) return null;
        return new Utilisateur.Builder()
                .setNom(utilisateurDto.nom())
                .setCourriel(utilisateurDto.courriel())
                .setMotPasse(utilisateurDto.motPasse())
                .build();
    }
}
