package com.dieguex.inventaireMaisonBackend.controller;

import com.dieguex.inventaireMaisonBackend.dto.ErreurResponseDto;
import com.dieguex.inventaireMaisonBackend.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UtilisateurNonTrouveException.class)
    public ResponseEntity<ErreurResponseDto> handleUtilisateurNonTrouveException(UtilisateurNonTrouveException exception){
        ErreurResponseDto erreur = new ErreurResponseDto(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erreur);
    }

    @ExceptionHandler(FamilleNonTrouveException.class)
    public ResponseEntity<ErreurResponseDto> handleFamilleNonTrouveException(FamilleNonTrouveException exception){
        ErreurResponseDto erreur = new ErreurResponseDto(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erreur);
    }

    @ExceptionHandler(UtilisateurExistantException.class)
    public ResponseEntity<ErreurResponseDto> handleUtilisateurExistantException(UtilisateurExistantException exception){
        ErreurResponseDto erreur = new ErreurResponseDto(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erreur);
    }

    @ExceptionHandler(LoginUtilisateurException.class)
    public ResponseEntity<ErreurResponseDto> handleLoginUtilisateurException(LoginUtilisateurException exception){
        ErreurResponseDto erreur = new ErreurResponseDto(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erreur);
    }

    @ExceptionHandler(ProduitException.class)
    public ResponseEntity<ErreurResponseDto> handleProduitException(ProduitException exception){
        ErreurResponseDto erreur = new ErreurResponseDto(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erreur);
    }

    @ExceptionHandler(UtilisateurException.class)
    public ResponseEntity<ErreurResponseDto> handleUtilisateurException(UtilisateurException exception){
        ErreurResponseDto erreur = new ErreurResponseDto(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);
    }

    @ExceptionHandler(FamilleException.class)
    public ResponseEntity<ErreurResponseDto> handleFamilleException(FamilleException exception){
        ErreurResponseDto erreur = new ErreurResponseDto(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErreurResponseDto> handleException(Exception exception){
        ErreurResponseDto erreur = new ErreurResponseDto(
                exception.getMessage(),
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erreur);
    }
}

