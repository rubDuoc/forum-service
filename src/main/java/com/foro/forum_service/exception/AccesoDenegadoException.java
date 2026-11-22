package com.foro.forum_service.exception;

// 403: el usuario existe pero su rol no le permite realizar la operacion
public class AccesoDenegadoException extends RuntimeException {

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }
}
