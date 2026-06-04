package com.dieguex.inventaireMaisonBackend.dto;

import com.dieguex.inventaireMaisonBackend.model.Utilisateur;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UtilisateurDto(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,
        @NotBlank(message = "Le courriel est obligatoire")
        @Email(message = "Le format du courriel est invalide")
        String courriel,
        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 5, message = "Le mot de passe doit contenir au moins 8 caractères")
        String motPasse,
        UUID uuid
) {
    public static UtilisateurDto versDto(Utilisateur utilisateur){
        if (utilisateur == null) return null;
        return new UtilisateurDto(
                utilisateur.getNom(),
                utilisateur.getCourriel(),
                null,
                utilisateur.getUuid()
        );
    }
    public static Utilisateur versEntite(UtilisateurDto utilisateurDto, String motDePasse){
        if (utilisateurDto == null) return null;
        return new Utilisateur.Builder()
                .setNom(utilisateurDto.nom())
                .setCourriel(utilisateurDto.courriel())
                .setMotPasse(motDePasse)
                .build();
    }
}
