package com.dieguex.inventaireMaisonBackend.service;

import com.dieguex.inventaireMaisonBackend.Exceptions.FamilleException;
import com.dieguex.inventaireMaisonBackend.Exceptions.ProduitException;
import com.dieguex.inventaireMaisonBackend.Exceptions.UtilisateurException;
import com.dieguex.inventaireMaisonBackend.dto.FamilleDto;
import com.dieguex.inventaireMaisonBackend.dto.ProduitDto;
import com.dieguex.inventaireMaisonBackend.dto.UtilisateurDto;
import com.dieguex.inventaireMaisonBackend.model.Famille;
import com.dieguex.inventaireMaisonBackend.model.Produit;
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
    public FamilleDto creerFamille(UUID utilisateurUuid, String nomFamille) throws UtilisateurException{
        Utilisateur utilisateur = utilisateurRepository.findByUuid(utilisateurUuid)
                .orElseThrow(() -> new UtilisateurException("Utilisateur non trouvé"));
        if (utilisateur.getFamille() != null) {
            throw new UtilisateurException("L'utilisateur est déjà dans une famille");
        }
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

        if (utilisateur.getFamille() != null) {
            throw new FamilleException("L'utilisateur est déjà dans une famille");
        }
        utilisateur.setFamille(famille);

        return UtilisateurDto.versDto(utilisateur);
    }

    @Transactional(rollbackFor = UtilisateurException.class)
    public UtilisateurDto creerUtilisateur(UtilisateurDto utilisateurDto) throws UtilisateurException {
        if (utilisateurRepository.existsByCourriel(utilisateurDto.courriel())) {
            throw new UtilisateurException("Un utilisateur avec cet courriel existe déjà");
        }
        Utilisateur utilisateur = UtilisateurDto.versEntite(utilisateurDto);
        return UtilisateurDto.versDto(utilisateurRepository.save(utilisateur));
    }

    @Transactional(rollbackFor = {ProduitException.class, FamilleException.class})
    public List<ProduitDto> creerProduit(List<ProduitDto> produitDtoList, UUID familleUuid) throws ProduitException, FamilleException {
        Famille famille = familleRepository.findByUuid(familleUuid).orElseThrow(
                () -> new FamilleException("Famille non trouvée")
        );
        List<Produit> produits = produitDtoList.stream().map(ProduitDto::versEntite).toList();
        produits.forEach((produit) -> {
            produit.setFamille(famille);
            famille.getListeProduits().add(produit);
        });
        return produitRepository.saveAll(produits).stream().map(ProduitDto::versDto).toList();
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

    public List<ProduitDto> obtenirProduitsParFamille(UUID familleUuid) throws FamilleException {
        List<Produit> produits = produitRepository.trouverProduitsParFamille(familleUuid).orElseThrow(
            () -> new FamilleException("Famille non trouvée")
        );
        return produits.stream().map(ProduitDto::versDto).toList();
    }

}
