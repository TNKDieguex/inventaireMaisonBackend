package com.dieguex.inventaireMaisonBackend.controller;

import com.dieguex.inventaireMaisonBackend.config.UtilisateurPrincipal;
import com.dieguex.inventaireMaisonBackend.dto.*;
import com.dieguex.inventaireMaisonBackend.exceptions.FamilleException;
import com.dieguex.inventaireMaisonBackend.exceptions.ProduitException;
import com.dieguex.inventaireMaisonBackend.model.Utilisateur;
import com.dieguex.inventaireMaisonBackend.service.ProduitService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {
    Logger logger = LoggerFactory.getLogger(ProduitController.class);

    private final ProduitService produitService;

    public ProduitController(ProduitService produitService){
        this.produitService = produitService;
    }

    @PostMapping("/creation")
    public ResponseEntity<List<ProduitDto>> creerProduit(@Valid @RequestBody List<ProduitDto> produitDtoList,
                                                         @AuthenticationPrincipal UtilisateurPrincipal utilisateurPrincipal) throws FamilleException{
        Utilisateur utilisateur = utilisateurPrincipal.getUtilisateur();
        if (utilisateur.getFamille() == null) {
            throw new FamilleException("L'utilisateur n'est pas associé à une famille");
        }
        UUID familleUuid = utilisateur.getFamille().getUuid();
        logger.info("Création de {} produits pour la famille : {}", produitDtoList.size(), familleUuid);
        List<ProduitDto> produitDtoListResultat = produitService.creerProduit(produitDtoList, familleUuid).orElseThrow();
        return ResponseEntity.status(HttpStatus.CREATED).body(produitDtoListResultat);
    }

    @PatchMapping("/{uuid}/quantite")
    public ResponseEntity<ProduitDto> modifierQuantiteProduit(@PathVariable UUID uuid,
                                                              @Valid @RequestBody UpdateQuantiteProduitDto updateQuantiteProduitDto,
                                                              @AuthenticationPrincipal UtilisateurPrincipal utilisateurPrincipal) throws ProduitException {
        UUID familleUuid = utilisateurPrincipal.getUtilisateur().getFamille().getUuid();
        logger.info("Modification de la quantité du produit : {} (Famille: {})", uuid, familleUuid);
        ProduitDto produitDto = produitService.modifierQuantiteProduit(uuid, updateQuantiteProduitDto.quantite(), familleUuid)
                .orElseThrow();
        return ResponseEntity.ok().body(produitDto);
    }

    @PatchMapping("/{uuid}/notes")
    public ResponseEntity<ProduitDto> modifierNotesProduit(@PathVariable UUID uuid,
                                                           @Valid @RequestBody UpdateNoteProduitDto updateNoteProduitDto,
                                                           @AuthenticationPrincipal UtilisateurPrincipal utilisateurPrincipal) throws ProduitException {
        UUID familleUuid = utilisateurPrincipal.getUtilisateur().getFamille().getUuid();
        logger.info("Modification des notes du produit : {} (Famille: {})", uuid, familleUuid);
        ProduitDto produitDto = produitService.modifierNotesProduit(uuid,updateNoteProduitDto.notes(), familleUuid)
                .orElseThrow();
        return ResponseEntity.ok().body(produitDto);
    }
    @PutMapping("/modifierProduit")
    public ResponseEntity<ProduitDto> modifierProduit(@Valid @RequestBody UpdateProduitDto produitDto,
                                                      @AuthenticationPrincipal UtilisateurPrincipal utilisateurPrincipal) throws ProduitException {
        UUID familleUuid = utilisateurPrincipal.getUtilisateur().getFamille().getUuid();
        logger.info("Modification sécurisée du produit : {} (Famille: {})", produitDto.uuid(), familleUuid);
        ProduitDto produitModifie = produitService.modifierProduit(produitDto, familleUuid).orElseThrow();
        return ResponseEntity.ok().body(produitModifie);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<ProduitDto> supprimerProduit(@PathVariable UUID uuid,
                                                       @AuthenticationPrincipal UtilisateurPrincipal utilisateurPrincipal) throws ProduitException {
        UUID familleUuid = utilisateurPrincipal.getUtilisateur().getFamille().getUuid();
        logger.info("Suppression sécurisée du produit : {} pour la famille : {}", uuid, familleUuid);
        ProduitDto produitDto = produitService.supprimerProduit(uuid, familleUuid).orElseThrow();
        return ResponseEntity.ok().body(produitDto);
    }

    @GetMapping
    public ResponseEntity<List<ProduitDto>> obtenirProduitsParFamille(@AuthenticationPrincipal UtilisateurPrincipal utilisateurPrincipal) throws FamilleException {
        UUID familleUuid = utilisateurPrincipal.getUtilisateur().getFamille().getUuid();
        logger.info("Récupération des produits de la famille : {}", familleUuid);
        List<ProduitDto> produitDto = produitService.obtenirProduitsParFamille(familleUuid);
        return ResponseEntity.ok().body(produitDto);
    }

    @GetMapping("/liste-alertes-achats")
    public ResponseEntity<List<ProduitDto>> genererListeAlertesEtAchats(@AuthenticationPrincipal UtilisateurPrincipal utilisateurPrincipal){
        UUID familleUuid = utilisateurPrincipal.getUtilisateur().getFamille().getUuid();
        logger.info("Génération de la liste d'alertes pour la famille: {}", familleUuid);
        List<ProduitDto> produitDto = produitService.genererListeAlertesEtAchats(familleUuid);
        return ResponseEntity.ok().body(produitDto);
    }
}
