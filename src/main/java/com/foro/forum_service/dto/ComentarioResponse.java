package com.foro.forum_service.dto;

import com.foro.forum_service.model.Comentario;

import java.time.LocalDateTime;

public record ComentarioResponse(
        Long id,
        String contenido,
        Long usuarioId,
        Long temaId,
        LocalDateTime fechaCreacion
) {
    public static ComentarioResponse from(Comentario comentario) {
        return new ComentarioResponse(
                comentario.getId(),
                comentario.getContenido(),
                comentario.getUsuarioId(),
                comentario.getTema().getId(),
                comentario.getFechaCreacion());
    }
}
