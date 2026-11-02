package com.foro.forum_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ComentarioRequest(

        @NotBlank(message = "El contenido es obligatorio")
        @Size(max = 1000, message = "El contenido no puede superar 1000 caracteres")
        String contenido,

        @NotNull(message = "El usuarioId es obligatorio")
        @Positive(message = "El usuarioId debe ser positivo")
        Long usuarioId
) {
}
