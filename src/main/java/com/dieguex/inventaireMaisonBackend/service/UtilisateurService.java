package com.dieguex.inventaireMaisonBackend.service;

import com.dieguex.inventaireMaisonBackend.Exceptions.FamilleException;
import com.dieguex.inventaireMaisonBackend.Exceptions.UtilisateurException;
import com.dieguex.inventaireMaisonBackend.dto.FamilleDto;
import com.dieguex.inventaireMaisonBackend.dto.UtilisateurDto;
import com.dieguex.inventaireMaisonBackend.model.Famille;
import com.dieguex.inventaireMaisonBackend.model.Utilisateur;
import com.dieguex.inventaireMaisonBackend.persistence.FamilleRepository;
import com.dieguex.inventaireMaisonBackend.persistence.ProduitRepository;
import com.dieguex.inventaireMaisonBackend.persistence.UtilisateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UtilisateurService {
    private final FamilleRepository familleRepository;
    private final ProduitRepository produitRepository;
    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(FamilleRepository familleRepository,
                              ProduitRepository produitRepository,
                              UtilisateurRepository utilisateurRepository) {
        this.familleRepository = familleRepository;
        this.produitRepository = produitRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Transactional(rollbackFor = UtilisateurException.class)
    public FamilleDto creerFamille(UUID utilisateurUuid, String nomFamille) throws UtilisateurException {
        Utilisateur utilisateur = utilisateurRepository.findByUuid(utilisateurUuid)
                .orElseThrow(() -> new UtilisateurException("Utilisateur non trouvé"));

        Famille famille = new Famille.Builder()
                .setNomFamille(nomFamille)
                .build();
        familleRepository.save(famille);
        utilisateur.setFamille(famille);

        return FamilleDto.versDto(famille);
    }
    @Transactional(rollbackFor = {UtilisateurException.class, FamilleException.class})
    public UtilisateurDto seJoindreAUneFamille(UUID utilisateurUuid, UUID familleUuid)throws UtilisateurException, FamilleException {
        Utilisateur utilisateur = utilisateurRepository.findByUuid(utilisateurUuid)
                .orElseThrow(() -> new UtilisateurException("Utilisateur non trouvé"));
        Famille famille = familleRepository.findByUuid(familleUuid)
                .orElseThrow(() -> new FamilleException("Famille non trouvée"));

        utilisateur.setFamille(famille);

        return UtilisateurDto.versDto(utilisateur);
    }

    public FamilleDto obtenirFamilleParUtilisateur(UUID utilisateurUuid) throws UtilisateurException {
        Utilisateur utilisateur = utilisateurRepository.findByUuid(utilisateurUuid).orElseThrow(
                () -> new UtilisateurException("Utilisateur non trouvé")
        );
        return FamilleDto.versDto(utilisateur.getFamille());
    }

    public List<UtilisateurDto> obtenirUtilisateursParFamille(UUID familleUuid) throws UtilisateurException {
        List<Utilisateur> utilisateurs = utilisateurRepository.trouverUtilisateursParFamille(familleUuid).orElseThrow(
                () -> new UtilisateurException("Famille non trouvée")
        );
        return utilisateurs.stream().map(UtilisateurDto::versDto).toList();
    }

}
