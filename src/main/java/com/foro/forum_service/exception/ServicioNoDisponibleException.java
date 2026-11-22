package com.foro.forum_service.exception;

// 503: no se pudo comunicar con otro microservicio (por ejemplo, user-service)
public class ServicioNoDisponibleException extends RuntimeException {

    public ServicioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
