package com.dieguex.inventaireMaisonBackend.persistence;

import com.dieguex.inventaireMaisonBackend.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
