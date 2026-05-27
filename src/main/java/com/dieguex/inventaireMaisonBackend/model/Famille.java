package com.dieguex.inventaireMaisonBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
public class Famille {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomFamille;

    @OneToMany(mappedBy = "famille", cascade = CascadeType.ALL)
    private List<Utilisateur> listeUtilisateurs;

    @OneToMany(mappedBy = "famille", cascade = CascadeType.ALL)
    private List<Produit> listeProduits;

    protected Famille(){}
    protected Famille(Builder builder){
        this.id = builder.id;
        this.nomFamille = builder.nomFamille;
        this.listeUtilisateurs = builder.listeUtilisateurs;
    }

    public static class Builder{
        private Long id;
        private String nomFamille;
        private List<Utilisateur> listeUtilisateurs;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }
        public Builder setNomFamille(String nomFamille) {
            this.nomFamille = nomFamille;
            return this;
        }
        public Builder setListeUtilisateurs(List<Utilisateur> listeUtilisateurs) {
            this.listeUtilisateurs = listeUtilisateurs;
            return this;
        }
        public Famille build(){
            return new Famille(this);
        }
    }
}
