package com.dieguex.inventaireMaisonBackend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Setter
@Getter
@ToString
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.PRIVATE)
    private UUID uuid;

    private String nom;
    private String courriel;
    private String motPasse;


    @ManyToOne
    @JoinColumn(name = "famille_id")
    private Famille famille;

    protected Utilisateur(){}
    protected Utilisateur(Builder builder){
        this.nom = builder.nom;
        this.courriel = builder.courriel;
        this.motPasse = builder.motPasse;
        this.famille = builder.famille;
    }
    @PrePersist
    protected void onCreate(){
        if (this.uuid == null) this.uuid = UUID.randomUUID();
    }

    public static class Builder{
        private String nom;
        private String courriel;
        private String motPasse;
        private Famille famille;

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
