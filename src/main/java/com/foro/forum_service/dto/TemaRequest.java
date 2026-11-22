package com.foro.forum_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// El autor no viaja en el cuerpo: se toma del header X-Usuario-Id
public record TemaRequest(

        @NotBlank(message = "El titulo es obligatorio")
        @Size(min = 5, max = 150, message = "El titulo debe tener entre 5 y 150 caracteres")
        String titulo,

        @NotBlank(message = "El contenido es obligatorio")
        @Size(max = 2000, message = "El contenido no puede superar 2000 caracteres")
        String contenido,

        @NotNull(message = "El categoriaId es obligatorio")
        @Positive(message = "El categoriaId debe ser positivo")
        Long categoriaId
) {
}
