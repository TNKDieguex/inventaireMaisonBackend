package com.dieguex.inventaireMaisonBackend.persistence;

import com.dieguex.inventaireMaisonBackend.model.Famille;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FamilleRepository extends JpaRepository<Famille, Long> {
    Optional<Famille> findByUuid(UUID uuid);
}
