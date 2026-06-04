package com.dieguex.inventaireMaisonBackend.dto;

import java.time.LocalDateTime;

public record ErreurResponseDto(
        String message,
        LocalDateTime timeStamp,
        int status
) {
}
