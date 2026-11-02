package com.foro.forum_service.dto;

// Resultado de la consulta de cantidad de temas por categoria
public record CategoriaResumen(
        Long id,
        String nombre,
        Long cantidadTemas
) {
}
