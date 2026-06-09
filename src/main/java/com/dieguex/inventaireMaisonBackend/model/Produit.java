package com.dieguex.inventaireMaisonBackend.model;

import com.dieguex.inventaireMaisonBackend.dto.UpdateProduitDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.PRIVATE)
    private UUID uuid;

    private String nom;
    private double quantite;
    private double quantiteMinimal;
    private LocalDate dateLimiteConsommation;
    private CategorieProduit categorieProduit;
    @Column(name = "notes", length = 100)
    private String notes;


    @ManyToOne
    @JoinColumn(name = "famille_id")
    private Famille famille;

    protected Produit(){}
    protected Produit(Builder builder){
        this.nom = builder.nom;
        this.quantite = builder.quantite;
        this.quantiteMinimal = builder.quantiteMinimal;
        this.dateLimiteConsommation = builder.dateLimiteConsommation;
        this.categorieProduit = builder.categorieProduit;
        this.famille = builder.famille;
        this.notes = builder.notes;
    }
    @PrePersist
    protected void onCreate(){
        if (this.uuid == null) this.uuid = UUID.randomUUID();
    }

    public static class Builder{
        private String nom;
        private double quantite;
        private double quantiteMinimal;
        private LocalDate dateLimiteConsommation;
        private CategorieProduit categorieProduit;
        private Famille famille;
        private String notes;

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
        public Builder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Produit build() {
            return new Produit(this);
        }
    }
    public void modifierProduit(UpdateProduitDto produitDto){
        this.quantite = produitDto.quantite();
        this.quantiteMinimal = produitDto.quantiteMinimal();
        this.dateLimiteConsommation = produitDto.dateLimiteConsommation();
        this.notes = produitDto.notes();
    }

}
