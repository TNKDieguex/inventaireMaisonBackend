package com.dieguex.inventaireMaisonBackend.controller;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<UtilisateurDto> seConnecter(@Valid @RequestBody LoginRequestDto loginRequestDto) throws LoginUtilisateurException, UtilisateurException {
        logger.info("Connexion d'un utilisateur : {}", loginRequestDto.courriel());
        UtilisateurDto utilisateurConnecte = utilisateurService.seConnecter(loginRequestDto).orElseThrow();
        return ResponseEntity.ok().body(utilisateurConnecte);
    }

    @PostMapping("/familles")
    public ResponseEntity<FamilleDto> creerFamille(@Valid @RequestBody CreationFamilleDto creationFamilleDto) throws UtilisateurException {
        logger.info("Création d'une famille : {}", creationFamilleDto.nomFamille());
        FamilleDto familleCree = utilisateurService.creerFamille(creationFamilleDto.utilisateurUuid(), creationFamilleDto.nomFamille()).orElseThrow();
        return ResponseEntity.status(HttpStatus.CREATED).body(familleCree);
    }

    @PostMapping("/rejoindre-famille")
    public ResponseEntity<UtilisateurDto> seJoindreAUneFamille(@RequestBody SeJoindreFamilleDto seJoindreFamilleDto) throws UtilisateurException, FamilleException {
        logger.info("Tentative d'association de l'utilisateur {} à la famille {}",
                seJoindreFamilleDto.utilisateurUuid(), seJoindreFamilleDto.familleUuid());
        UtilisateurDto utilisateurConnecte = utilisateurService.seJoindreAUneFamille(seJoindreFamilleDto.utilisateurUuid(), seJoindreFamilleDto.familleUuid()).orElseThrow();
        return ResponseEntity.ok().body(utilisateurConnecte);
    }

    @GetMapping("/famille-code")
    public ResponseEntity<FamilleDto> obtenirCodeFamille(@RequestParam(name = "id", required = true) UUID utilisateurUuid) throws UtilisateurException {
        logger.info("Récupération du code de la famille de l'utilisateur : {}", utilisateurUuid);
        FamilleDto familleDto = utilisateurService.obtenirFamilleParUtilisateur(utilisateurUuid);
        return ResponseEntity.ok().body(familleDto);
    }

    @GetMapping("/familles/mes-membres")
    public ResponseEntity<List<UtilisateurDto>> obtenirUtilisateursParFamille(@RequestParam(name = "id", required = true) UUID familleUuid) throws UtilisateurException {
        logger.info("Récupération des membres de la famille : {}", familleUuid);
        List<UtilisateurDto> utilisateurDto = utilisateurService.obtenirUtilisateursParFamille(familleUuid);
        return ResponseEntity.ok().body(utilisateurDto);
    }
}
