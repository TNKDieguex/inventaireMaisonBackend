package com.dieguex.inventaireMaisonBackend.controller;

import com.dieguex.inventaireMaisonBackend.dto.UtilisateurDto;
import com.dieguex.inventaireMaisonBackend.exceptions.UtilisateurException;
import com.dieguex.inventaireMaisonBackend.service.UtilisateurService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
    Logger logger = LoggerFactory.getLogger(UtilisateurController.class);

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService){
        this.utilisateurService = utilisateurService;
    }

    @PostMapping
    public ResponseEntity<UtilisateurDto> creerUtilisateur(@Valid @RequestBody UtilisateurDto utilisateurDto) throws UtilisateurException {
        logger.info("Création d'un utilisateur : {}", utilisateurDto);
        UtilisateurDto utilisateurCree = utilisateurService.creerUtilisateur(utilisateurDto).orElseThrow();
        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateurCree);
    }
}
