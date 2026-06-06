package com.dieguex.inventaireMaisonBackend.service;

import com.dieguex.inventaireMaisonBackend.dto.ProduitDto;
import com.dieguex.inventaireMaisonBackend.exceptions.FamilleException;
import com.dieguex.inventaireMaisonBackend.exceptions.FamilleNonTrouveException;
import com.dieguex.inventaireMaisonBackend.exceptions.ProduitException;
import com.dieguex.inventaireMaisonBackend.model.Famille;
import com.dieguex.inventaireMaisonBackend.model.Produit;
import com.dieguex.inventaireMaisonBackend.persistence.FamilleRepository;
import com.dieguex.inventaireMaisonBackend.persistence.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProduitService {
    private final FamilleRepository familleRepository;
    private final ProduitRepository produitRepository;

    public ProduitService(FamilleRepository familleRepository,
                              ProduitRepository produitRepository) {
        this.familleRepository = familleRepository;
        this.produitRepository = produitRepository;
    }


    @Transactional(rollbackFor = {ProduitException.class, FamilleException.class})
    public Optional<List<ProduitDto>> creerProduit(List<ProduitDto> produitDtoList, UUID familleUuid) throws FamilleException {
        Famille famille = familleRepository.findByUuid(familleUuid).orElseThrow(
                () -> new FamilleNonTrouveException("Famille non trouvée"));

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

    public List<ProduitDto> obtenirProduitsParFamille(UUID familleUuid) throws FamilleException {
        if (!familleRepository.existsByUuid(familleUuid)) {
            throw new FamilleNonTrouveException("Famille non trouvée");
        }

        List<Produit> produits = produitRepository.trouverProduitsParFamille(familleUuid);
        return produits.stream().map(ProduitDto::versDto).toList();
    }

    public List<ProduitDto> genererListeAlertesEtAchats(){
        LocalDate aujourdHui = LocalDate.now();
        LocalDate dateLimite = aujourdHui.plusDays(7);

        List<Produit> produits = produitRepository.findProduitsAlerte(dateLimite);
        return produits.stream().map(ProduitDto::versDto).toList();
    }
}
