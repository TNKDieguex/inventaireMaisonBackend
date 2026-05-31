package com.dieguex.inventaireMaisonBackend.dto;

import com.dieguex.inventaireMaisonBackend.model.CategorieProduit;
import com.dieguex.inventaireMaisonBackend.model.Produit;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record ProduitDto(
        String nom,
        double quantite,
        double quantiteMinimal,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dateLimiteConsommation,
        CategorieProduit categorieProduit
) {
    public static ProduitDto versDto(Produit produit){
        if (produit == null) return null;
        return new ProduitDto(
                produit.getNom(),
                produit.getQuantite(),
                produit.getQuantiteMinimal(),
                produit.getDateLimiteConsommation(),
                produit.getCategorieProduit()
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
                .build();
    }
}
