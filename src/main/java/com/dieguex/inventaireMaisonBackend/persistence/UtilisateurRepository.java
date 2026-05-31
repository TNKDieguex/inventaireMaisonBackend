package com.dieguex.inventaireMaisonBackend.persistence;

import com.dieguex.inventaireMaisonBackend.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByUuid(UUID familleUuid);

    @Query(
        """
            SELECT u FROM Utilisateur u
            JOIN u.famille f
            WHERE f.uuid = :familleUuid
        """)
    Optional<List<Utilisateur>> trouverUtilisateursParFamille(@Param("familleUuid") UUID familleUuid);

    boolean existsByCourriel(String email);
}
