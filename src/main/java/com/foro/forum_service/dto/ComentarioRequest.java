package com.foro.forum_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// El autor no viaja en el cuerpo: se toma del header X-Usuario-Id
public record ComentarioRequest(

        @NotBlank(message = "El contenido es obligatorio")
        @Size(max = 1000, message = "El contenido no puede superar 1000 caracteres")
        String contenido
) {
}
