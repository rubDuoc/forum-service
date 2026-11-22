package com.foro.forum_service.exception;

// 409: la operacion choca con el estado actual de los datos (por ejemplo, un registro duplicado)
public class ConflictoException extends RuntimeException {

    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
