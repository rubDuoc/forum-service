package com.foro.forum_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Banear (true) o desbanear (false) un tema o comentario
public record ModeracionRequest(

        @NotNull(message = "Indica si el contenido queda baneado (true) o no (false)")
        Boolean baneado,

        @Size(max = 300, message = "El motivo no puede superar 300 caracteres")
        String motivo
) {
}
