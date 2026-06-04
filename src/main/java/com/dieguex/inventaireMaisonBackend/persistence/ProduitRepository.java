package com.dieguex.inventaireMaisonBackend.persistence;

import com.dieguex.inventaireMaisonBackend.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    @Query(
        """
            SELECT p FROM Produit p
            JOIN p.famille f
            WHERE f.uuid = :familleUuid
        """)
    Optional<List<Produit>> trouverProduitsParFamille(@Param("familleUuid") UUID familleUuid);
    Optional<Produit> findByUuid(UUID uuid);
}
