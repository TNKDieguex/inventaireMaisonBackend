package com.dieguex.inventaireMaisonBackend.controller;

import com.dieguex.inventaireMaisonBackend.dto.*;
import com.dieguex.inventaireMaisonBackend.exceptions.FamilleException;
import com.dieguex.inventaireMaisonBackend.exceptions.ProduitException;
import com.dieguex.inventaireMaisonBackend.service.ProduitService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ProduitDto>> creerProduit(@Valid @RequestBody CreationProduitsDto creationProduitsDto) throws FamilleException{
        logger.info("Création des produits : {}", creationProduitsDto.produitDtoList().size() + " produits");
        List<ProduitDto> produitDtoList = produitService.creerProduit(creationProduitsDto.produitDtoList(), creationProduitsDto.familleUuid()).orElseThrow();
        return ResponseEntity.status(HttpStatus.CREATED).body(produitDtoList);
    }

    @PatchMapping("/{uuid}/quantite")
    public ResponseEntity<ProduitDto> modifierQuantiteProduit(@PathVariable UUID uuid,
                                                              @Valid @RequestBody UpdateQuantiteProduitDto updateQuantiteProduitDto) throws ProduitException {
        logger.info("Modification de la quantité du produit : {}", uuid);
        ProduitDto produitDto = produitService.modifierQuantiteProduit(uuid, updateQuantiteProduitDto.quantite())
                .orElseThrow();
        return ResponseEntity.ok().body(produitDto);
    }

    @PatchMapping("/{uuid}/notes")
    public ResponseEntity<ProduitDto> modifierNotesProduit(@PathVariable UUID uuid,
                                                           @Valid @RequestBody UpdateNoteProduitDto updateNoteProduitDto) throws ProduitException {
        logger.info("Modification des notes du produit : {}", uuid);
        ProduitDto produitDto = produitService.modifierNotesProduit(uuid,updateNoteProduitDto.notes())
                .orElseThrow();
        return ResponseEntity.ok().body(produitDto);
    }
    @PutMapping("/modifierProduit")
    public ResponseEntity<ProduitDto> modifierProduit(@Valid @RequestBody UpdateProduitDto produitDto) throws ProduitException {
        logger.info("Modification du produit : {}", produitDto.uuid());
        ProduitDto produitModifie = produitService.modifierProduit(produitDto).orElseThrow();
        return ResponseEntity.ok().body(produitModifie);
    }

    @DeleteMapping("/{uuid}/effacerProdutit")
    public ResponseEntity<ProduitDto> supprimerProduit(@PathVariable UUID uuid) throws ProduitException {
        logger.info("Suppression du produit : {}", uuid);
        ProduitDto produitDto = produitService.supprimerProduit(uuid).orElseThrow();
        return ResponseEntity.ok().body(produitDto);
    }

    @GetMapping("/famille-code")
    public ResponseEntity<List<ProduitDto>> obtenirProduitsParFamille(@RequestParam(name = "id", required = true) UUID familleUuid) throws FamilleException {
        logger.info("Récupération des produits de la famille : {}", familleUuid);
        List<ProduitDto> produitDto = produitService.obtenirProduitsParFamille(familleUuid);
        return ResponseEntity.ok().body(produitDto);
    }

    @GetMapping("/liste-alertes-achats")
    public ResponseEntity<List<ProduitDto>> genererListeAlertesEtAchats(){
        logger.info("Génération de la liste d'alertes et d'achats");
        List<ProduitDto> produitDto = produitService.genererListeAlertesEtAchats();
        return ResponseEntity.ok().body(produitDto);
    }
}
