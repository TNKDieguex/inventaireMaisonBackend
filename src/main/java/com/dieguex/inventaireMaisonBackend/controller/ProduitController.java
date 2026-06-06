package com.dieguex.inventaireMaisonBackend.controller;

import com.dieguex.inventaireMaisonBackend.dto.CreationProduitsDto;
import com.dieguex.inventaireMaisonBackend.dto.ProduitDto;
import com.dieguex.inventaireMaisonBackend.exceptions.FamilleException;
import com.dieguex.inventaireMaisonBackend.service.ProduitService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


}
