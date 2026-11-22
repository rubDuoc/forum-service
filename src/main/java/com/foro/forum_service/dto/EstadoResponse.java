package com.foro.forum_service.dto;

import lombok.Builder;

import java.time.LocalDateTime;

// Patron Builder: la respuesta se arma paso a paso con EstadoResponse.builder()...build()
@Builder
public record EstadoResponse(String servicio, String estado, LocalDateTime fecha) {
}
