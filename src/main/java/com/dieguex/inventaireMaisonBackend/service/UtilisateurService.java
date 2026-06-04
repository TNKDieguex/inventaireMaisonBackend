package com.dieguex.inventaireMaisonBackend.service;

import com.dieguex.inventaireMaisonBackend.dto.LoginRequestDto;
import com.dieguex.inventaireMaisonBackend.exceptions.FamilleException;
import com.dieguex.inventaireMaisonBackend.exceptions.ProduitException;
import com.dieguex.inventaireMaisonBackend.exceptions.UtilisateurException;
import com.dieguex.inventaireMaisonBackend.dto.FamilleDto;
import com.dieguex.inventaireMaisonBackend.dto.ProduitDto;
import com.dieguex.inventaireMaisonBackend.dto.UtilisateurDto;
import com.dieguex.inventaireMaisonBackend.model.Famille;
import com.dieguex.inventaireMaisonBackend.model.Produit;
import com.dieguex.inventaireMaisonBackend.model.Utilisateur;
import com.dieguex.inventaireMaisonBackend.persistence.FamilleRepository;
import com.dieguex.inventaireMaisonBackend.persistence.ProduitRepository;
import com.dieguex.inventaireMaisonBackend.persistence.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UtilisateurService {
    private final FamilleRepository familleRepository;
    private final ProduitRepository produitRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UtilisateurService(FamilleRepository familleRepository,
                              ProduitRepository produitRepository,
                              UtilisateurRepository utilisateurRepository) {
        this.familleRepository = familleRepository;
        this.produitRepository = produitRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Transactional(rollbackFor = UtilisateurException.class)
    public Optional<FamilleDto> creerFamille(UUID utilisateurUuid, String nomFamille) throws UtilisateurException{
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

        return Optional.of(FamilleDto.versDto(famille));
    }

    @Transactional(rollbackFor = {UtilisateurException.class, FamilleException.class})
    public Optional<UtilisateurDto> seJoindreAUneFamille(UUID utilisateurUuid, UUID familleUuid)throws UtilisateurException, FamilleException {
        Utilisateur utilisateur = utilisateurRepository.findByUuid(utilisateurUuid)
                .orElseThrow(() -> new UtilisateurException("Utilisateur non trouvé"));
        Famille famille = familleRepository.findByUuid(familleUuid)
                .orElseThrow(() -> new FamilleException("Famille non trouvée"));

        if (utilisateur.getFamille() != null) {
            throw new FamilleException("L'utilisateur est déjà dans une famille");
        }
        utilisateur.setFamille(famille);

        return Optional.of(UtilisateurDto.versDto(utilisateur));
    }

    @Transactional(rollbackFor = UtilisateurException.class)
    public Optional<UtilisateurDto> creerUtilisateur(UtilisateurDto utilisateurDto) throws UtilisateurException {
        String motDePasseHache = passwordEncoder.encode(utilisateurDto.motPasse());

        if (utilisateurRepository.existsByCourriel(utilisateurDto.courriel())) {
            throw new UtilisateurException("Un utilisateur avec cet courriel existe déjà");
        }
        Utilisateur utilisateur = UtilisateurDto.versEntite(utilisateurDto, motDePasseHache);
        return Optional.of(UtilisateurDto.versDto(utilisateurRepository.save(utilisateur)));
    }

    @Transactional(rollbackFor = {ProduitException.class, FamilleException.class})
    public Optional<List<ProduitDto>> creerProduit(List<ProduitDto> produitDtoList, UUID familleUuid) throws ProduitException, FamilleException {
        Famille famille = familleRepository.findByUuid(familleUuid).orElseThrow(
                () -> new FamilleException("Famille non trouvée"));

        List<Produit> produits = produitDtoList.stream().map(ProduitDto::versEntite).toList();
        produits.forEach((produit) -> {
            produit.setFamille(famille);
            famille.getListeProduits().add(produit);
        });
        return Optional.of(produitRepository.saveAll(produits).stream().map(ProduitDto::versDto).toList());
    }

    @Transactional(rollbackFor = ProduitException.class)
    public Optional<ProduitDto> supprimerProduit(UUID produitUuid) throws ProduitException {
        Produit produit = produitRepository.findByUuid(produitUuid).orElseThrow(
                () -> new ProduitException("Produit non trouvé"));

        if (produit.getFamille() != null) {
            produit.getFamille().supprimerProduit(produit);
        }

        produitRepository.delete(produit);
        return Optional.of(ProduitDto.versDto(produit));
    }

    @Transactional(rollbackFor = ProduitException.class)
    public Optional<ProduitDto> modifierProduit(ProduitDto produitDto) throws ProduitException{
        Produit produit = produitRepository.findByUuid(produitDto.uuid()).orElseThrow(
                () -> new ProduitException("Produit non trouvé"));

        produit.modifierProduit(produitDto);
        return Optional.of(ProduitDto.versDto(produit));
    }

    public Optional<UtilisateurDto> seConnecter(LoginRequestDto loginRequestDto) throws UtilisateurException{
        Utilisateur utilisateur = utilisateurRepository.findByCourriel(loginRequestDto.courriel()).orElseThrow(
                () -> new UtilisateurException("Utilisateur non trouvé"));
        boolean motDePasseValide = passwordEncoder.matches(loginRequestDto.motPasse(), utilisateur.getMotPasse());

        if (!motDePasseValide) {
            throw new UtilisateurException("Mot de passe incorrect");
        }
        return Optional.of(UtilisateurDto.versDto(utilisateur));
    }

    public FamilleDto obtenirFamilleParUtilisateur(UUID utilisateurUuid) throws UtilisateurException {
        Utilisateur utilisateur = utilisateurRepository.findByUuid(utilisateurUuid).orElseThrow(
                () -> new UtilisateurException("Utilisateur non trouvé"));

        return FamilleDto.versDto(utilisateur.getFamille());
    }

    public List<UtilisateurDto> obtenirUtilisateursParFamille(UUID familleUuid) throws UtilisateurException {
        List<Utilisateur> utilisateurs = utilisateurRepository.trouverUtilisateursParFamille(familleUuid).orElseThrow(
                () -> new UtilisateurException("Famille non trouvée"));

        return utilisateurs.stream().map(UtilisateurDto::versDto).toList();
    }

    public List<ProduitDto> obtenirProduitsParFamille(UUID familleUuid) throws FamilleException {
        List<Produit> produits = produitRepository.trouverProduitsParFamille(familleUuid).orElseThrow(
            () -> new FamilleException("Famille non trouvée"));

        return produits.stream().map(ProduitDto::versDto).toList();
    }
}
