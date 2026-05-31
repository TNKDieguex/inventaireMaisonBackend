package com.dieguex.inventaireMaisonBackend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Famille {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PRIVATE)
    private Long id;
    private String nomFamille;

    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.PRIVATE)
    private UUID uuid;


    @OneToMany(mappedBy = "famille", cascade = CascadeType.ALL)
    private List<Utilisateur> listeUtilisateurs = new ArrayList<>();

    @OneToMany(mappedBy = "famille", cascade = CascadeType.ALL)
    private List<Produit> listeProduits = new ArrayList<>();

    protected Famille(){}
    protected Famille(Builder builder){
        this.nomFamille = builder.nomFamille;
        if (builder.listeUtilisateurs != null) this.listeUtilisateurs = builder.listeUtilisateurs;
        if (builder.listeProduits != null) this.listeProduits = builder.listeProduits;
    }
    @PrePersist
    protected void onCreate(){
        if (this.uuid == null) this.uuid = UUID.randomUUID();
    }

    public static class Builder{
        private String nomFamille;
        private List<Utilisateur> listeUtilisateurs;
        private List<Produit> listeProduits;

        public Builder setNomFamille(String nomFamille) {
            this.nomFamille = nomFamille;
            return this;
        }
        public Builder setListeUtilisateurs(List<Utilisateur> listeUtilisateurs) {
            this.listeUtilisateurs = listeUtilisateurs;
            return this;
        }
        public Builder setListeProduits(List<Produit> listeProduits) {
            this.listeProduits = listeProduits;
            return this;
        }
        public Famille build(){
            return new Famille(this);
        }
    }
}
