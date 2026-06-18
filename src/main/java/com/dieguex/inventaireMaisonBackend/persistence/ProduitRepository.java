package com.dieguex.inventaireMaisonBackend.persistence;

import com.dieguex.inventaireMaisonBackend.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    @Query("""
            SELECT p FROM Produit p
            WHERE p.famille.uuid = :familleUuid
            """)
    List<Produit> trouverProduitsParFamille(@Param("familleUuid") UUID familleUuid);
    @Query("""
            SELECT p FROM Produit p WHERE p.famille.uuid = :familleUuid 
                AND ((p.quantite <= p.quantiteMinimal)
                OR (p.dateLimiteConsommation IS NOT NULL
                AND p.dateLimiteConsommation <= :dateLimite AND p.quantite > 0))
       """)
    List<Produit> findProduitsAlerte(@Param("dateLimite")LocalDate dateLimite,
    @Param("familleUuid")UUID familleUuid);
    Optional<Produit> findByUuidAndFamilleUuid(UUID uuid, UUID familleUuid);

}
