package com.dieguex.inventaireMaisonBackend.dto;

import com.dieguex.inventaireMaisonBackend.model.CategorieProduit;
import com.dieguex.inventaireMaisonBackend.model.Produit;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record ProduitDto(
        @Size(min = 3, message = "Le nom doit contenir au moins 3 caractères")
        @Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
        @NotNull(message = "Le nom est obligatoire")
        String nom,
        @NotNull(message = "La quantité est obligatoire")
        double quantite,
        @NotNull(message = "La quantité minimale est obligatoire")
        double quantiteMinimal,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "La date limite de consommation est obligatoire")
        LocalDate dateLimiteConsommation,
        @NotNull(message = "La catégorie du produit est obligatoire")
        CategorieProduit categorieProduit,
        @Size(max = 100, message = "Les notes ne peuvent pas dépasser 100 caractères")
        String notes,
        UUID uuid
) {
    public static ProduitDto versDto(Produit produit){
        if (produit == null) return null;
        return new ProduitDto(
                produit.getNom(),
                produit.getQuantite(),
                produit.getQuantiteMinimal(),
                produit.getDateLimiteConsommation(),
                produit.getCategorieProduit(),
                produit.getNotes(),
                produit.getUuid()
        );
    }
    public static Produit versEntite(ProduitDto produitDto){
        if (produitDto == null) return null;
        return new Produit.Builder()
                .setNom(produitDto.nom())
                .setQuantite(produitDto.quantite())
                .setQuantiteMinimal(produitDto.quantiteMinimal())
                .setDateLimiteConsommation(produitDto.dateLimiteConsommation())
                .setCategorieProduit(produitDto.categorieProduit())
                .setNotes(produitDto.notes())
                .build();
    }
}
