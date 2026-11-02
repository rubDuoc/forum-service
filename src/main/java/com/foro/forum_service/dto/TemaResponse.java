package com.foro.forum_service.dto;

import com.foro.forum_service.model.Tema;

import java.time.LocalDateTime;

public record TemaResponse(
        Long id,
        String titulo,
        String contenido,
        Long usuarioId,
        Long categoriaId,
        String categoria,
        LocalDateTime fechaCreacion
) {
    public static TemaResponse from(Tema tema) {
        return new TemaResponse(
                tema.getId(),
                tema.getTitulo(),
                tema.getContenido(),
                tema.getUsuarioId(),
                tema.getCategoria().getId(),
                tema.getCategoria().getNombre(),
                tema.getFechaCreacion());
    }
}
