package com.foro.forum_service.dto;

import lombok.Builder;

// Estado de moderacion de un contenido (patron Builder)
@Builder
public record ModeracionResponse(String tipo, Long id, boolean baneado, String motivo) {
}
