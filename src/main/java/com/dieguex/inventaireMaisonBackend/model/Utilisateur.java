package com.dieguex.inventaireMaisonBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String courriel;
    private String motPasse;

    @ManyToOne
    @JoinColumn(name = "famille_id")
    private Famille famille;

    protected Utilisateur(){}
    protected Utilisateur(Builder builder){
        this.id = builder.id;
        this.nom = builder.nom;
        this.courriel = builder.courriel;
        this.motPasse = builder.motPasse;
        this.famille = builder.famille;
    }

    public static class Builder{
        private Long id;
        private String nom;
        private String courriel;
        private String motPasse;
        private Famille famille;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setNom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder setCourriel(String courriel) {
            this.courriel = courriel;
            return this;
        }

        public Builder setMotPasse(String motPasse) {
            this.motPasse = motPasse;
            return this;
        }

        public Builder setFamille(Famille famille) {
            this.famille = famille;
            return this;
        }

        public Utilisateur build() {
            return new Utilisateur(this);
        }

    }

}
