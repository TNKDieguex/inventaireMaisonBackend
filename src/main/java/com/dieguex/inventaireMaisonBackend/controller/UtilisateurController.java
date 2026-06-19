package com.dieguex.inventaireMaisonBackend.controller;

import com.dieguex.inventaireMaisonBackend.config.UtilisateurPrincipal;
import com.dieguex.inventaireMaisonBackend.dto.*;
import com.dieguex.inventaireMaisonBackend.exceptions.FamilleException;
import com.dieguex.inventaireMaisonBackend.exceptions.LoginUtilisateurException;
import com.dieguex.inventaireMaisonBackend.exceptions.UtilisateurException;
import com.dieguex.inventaireMaisonBackend.service.UtilisateurService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


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

    @PostMapping("/connexion")
    public ResponseEntity<AuthResponseDto> seConnecter(@Valid @RequestBody LoginRequestDto loginRequestDto) throws LoginUtilisateurException, UtilisateurException {
        logger.info("Connexion d'un utilisateur : {}", loginRequestDto.courriel());

        AuthResponseDto utilisateurConnecte = utilisateurService.seConnecter(loginRequestDto).orElseThrow();
        return ResponseEntity.ok().body(utilisateurConnecte);
    }

    @PostMapping("/familles")
    public ResponseEntity<AuthResponseDto> creerFamille(@Valid @RequestBody CreationFamilleDto creationFamilleDto,
                                                   @AuthenticationPrincipal UtilisateurPrincipal utilisateurPrincipal) throws UtilisateurException {
        logger.info("Création d'une famille : {}", creationFamilleDto.nomFamille());
        UUID utilisateurUuid = utilisateurPrincipal.getUtilisateur().getUuid();
        AuthResponseDto authResponseDto = utilisateurService.creerFamille(utilisateurUuid, creationFamilleDto.nomFamille());
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDto);
    }

    @PostMapping("/rejoindre-famille")
    public ResponseEntity<AuthResponseDto> seJoindreAUneFamille(@RequestBody RejoindreFamilleDto rejoindreFamilleDto,
                                                               @AuthenticationPrincipal UtilisateurPrincipal utilisateurPrincipal) throws UtilisateurException, FamilleException {
        UUID utilisateurUuid = utilisateurPrincipal.getUtilisateur().getUuid();
        logger.info("L'utilisateur connecté {} tente de rejoindre la famille {}",
                utilisateurUuid, rejoindreFamilleDto.familleUuid());
        AuthResponseDto utilisateurModifie = utilisateurService
                .seJoindreAUneFamille(utilisateurUuid, rejoindreFamilleDto.familleUuid());
        return ResponseEntity.ok().body(utilisateurModifie);
    }

    @GetMapping("/familles/info")
    public ResponseEntity<FamilleDto> obtenirInfoFamille(@AuthenticationPrincipal UtilisateurPrincipal utilisateurPrincipal) throws UtilisateurException {
        UUID familleUuid = utilisateurPrincipal.getUtilisateur().getFamille().getUuid();
        logger.info("Récupération des membres de la famille : {}", familleUuid);
        FamilleDto familleDto = utilisateurService.obtenirInfoFamille(familleUuid);
        return ResponseEntity.ok().body(familleDto);
    }
}
