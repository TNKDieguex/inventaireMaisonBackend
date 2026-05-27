package com.dieguex.inventaireMaisonBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private double quantite;
    private double quantiteMinimal;
    private LocalDate dateLimiteConsommation;
    private CategorieProduit categorieProduit;

    @ManyToOne
    @JoinColumn(name = "famille_id")
    private Famille famille;

    protected Produit(){}
    protected Produit(Builder builder){
        this.id = builder.id;
        this.nom = builder.nom;
        this.quantite = builder.quantite;
        this.quantiteMinimal = builder.quantiteMinimal;
        this.dateLimiteConsommation = builder.dateLimiteConsommation;
        this.categorieProduit = builder.categorieProduit;
        this.famille = builder.famille;
    }

    public static class Builder{
        private Long id;
        private String nom;
        private double quantite;
        private double quantiteMinimal;
        private LocalDate dateLimiteConsommation;
        private CategorieProduit categorieProduit;
        private Famille famille;


        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setNom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder setQuantite(double quantite) {
            this.quantite = quantite;
            return this;
        }

        public Builder setQuantiteMinimal(double quantiteMinimal) {
            this.quantiteMinimal = quantiteMinimal;
            return this;
        }

        public Builder setDateLimiteConsommation(LocalDate dateLimiteConsommation) {
            this.dateLimiteConsommation = dateLimiteConsommation;
            return this;
        }

        public Builder setCategorieProduit(CategorieProduit categorieProduit) {
            this.categorieProduit = categorieProduit;
            return this;
        }
        public Builder setFamille(Famille famille) {
            this.famille = famille;
            return this;
        }

        public Produit build() {
            return new Produit(this);
        }
    }


}
