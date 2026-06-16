package com.dieguex.inventaireMaisonBackend.service;

import com.dieguex.inventaireMaisonBackend.config.UtilisateurPrincipal;
import com.dieguex.inventaireMaisonBackend.dto.AuthResponseDto;
import com.dieguex.inventaireMaisonBackend.dto.LoginRequestDto;
import com.dieguex.inventaireMaisonBackend.exceptions.*;
import com.dieguex.inventaireMaisonBackend.dto.FamilleDto;
import com.dieguex.inventaireMaisonBackend.dto.UtilisateurDto;
import com.dieguex.inventaireMaisonBackend.model.Famille;
import com.dieguex.inventaireMaisonBackend.model.Utilisateur;
import com.dieguex.inventaireMaisonBackend.persistence.FamilleRepository;
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
    private final UtilisateurRepository utilisateurRepository;
    private final JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UtilisateurService(FamilleRepository familleRepository,
                              UtilisateurRepository utilisateurRepository,
                              JwtService jwtService) {
        this.familleRepository = familleRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.jwtService = jwtService;
    }

    @Transactional(rollbackFor = UtilisateurException.class)
    public Optional<UtilisateurDto> creerUtilisateur(UtilisateurDto utilisateurDto) throws UtilisateurException {
        String motDePasseHache = passwordEncoder.encode(utilisateurDto.motPasse());

        if (utilisateurRepository.existsByCourriel(utilisateurDto.courriel())) {
            throw new UtilisateurExistantException("Un utilisateur avec cet courriel existe déjà");
        }
        Utilisateur utilisateur = UtilisateurDto.versEntite(utilisateurDto, motDePasseHache);
        return Optional.of(UtilisateurDto.versDto(utilisateurRepository.save(utilisateur)));
    }

    @Transactional(rollbackFor = UtilisateurException.class)
    public Optional<FamilleDto> creerFamille(UUID utilisateurUuid, String nomFamille) throws UtilisateurException{
        Utilisateur utilisateur = utilisateurRepository.findByUuid(utilisateurUuid)
                .orElseThrow(() -> new UtilisateurNonTrouveException("Utilisateur non trouvé"));
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
                .orElseThrow(() -> new UtilisateurNonTrouveException("Utilisateur non trouvé"));
        Famille famille = familleRepository.findByUuid(familleUuid)
                .orElseThrow(() -> new FamilleNonTrouveException("Famille non trouvée"));

        if (utilisateur.getFamille() != null) {
            throw new FamilleException("L'utilisateur est déjà dans une famille");
        }
        utilisateur.setFamille(famille);

        return Optional.of(UtilisateurDto.versDto(utilisateur));
    }

    public Optional<AuthResponseDto> seConnecter(LoginRequestDto loginRequestDto) throws UtilisateurException, LoginUtilisateurException {
        Utilisateur utilisateur = utilisateurRepository.findByCourriel(loginRequestDto.courriel()).orElseThrow(
                () -> new UtilisateurNonTrouveException("Utilisateur non trouvé"));
        boolean motDePasseValide = passwordEncoder.matches(loginRequestDto.motPasse(), utilisateur.getMotPasse());

        if (!motDePasseValide) {
            throw new LoginUtilisateurException("Mot de passe incorrect");
        }
        String jwtToken = jwtService.generateToken(new UtilisateurPrincipal(utilisateur));
        UtilisateurDto utilisateurDto = UtilisateurDto.versDto(utilisateur);
        return Optional.of(new AuthResponseDto(jwtToken, utilisateurDto));
    }

    public FamilleDto obtenirFamilleParUtilisateur(UUID utilisateurUuid) throws UtilisateurException {
        Utilisateur utilisateur = utilisateurRepository.findByUuid(utilisateurUuid).orElseThrow(
                () -> new UtilisateurNonTrouveException("Utilisateur non trouvé"));

        return FamilleDto.versDto(utilisateur.getFamille());
    }

    public List<UtilisateurDto> obtenirUtilisateursParFamille(UUID familleUuid) throws UtilisateurException {
        List<Utilisateur> utilisateurs = utilisateurRepository.trouverUtilisateursParFamille(familleUuid).orElseThrow(
                () -> new UtilisateurNonTrouveException("Aucun utilisateur trouvé dans cette famille"));

        return utilisateurs.stream().map(UtilisateurDto::versDto).toList();
    }
}
