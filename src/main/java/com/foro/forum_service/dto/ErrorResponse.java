package com.foro.forum_service.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

// Patron Builder: todas las respuestas de error de la API tienen el mismo formato JSON
@Builder
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        Map<String, String> detalles
) {
}
