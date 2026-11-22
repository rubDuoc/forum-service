package com.foro.forum_service.service;

import com.foro.forum_service.client.UsuarioClient;
import com.foro.forum_service.client.UsuarioClient.UsuarioRemoto;
import com.foro.forum_service.exception.AccesoDenegadoException;
import org.springframework.stereotype.Service;

// Privilegios por rol: USUARIO solo modifica lo suyo; ADMIN puede modificar todo y gestionar categorias
@Service
public class AutorizacionService {

    private final UsuarioClient usuarioClient;

    public AutorizacionService(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    // Cualquier usuario registrado (para publicar temas y comentarios)
    public UsuarioRemoto validarUsuario(Long actorId) {
        return usuarioClient.obtenerUsuario(actorId);
    }

    // El autor del contenido o un ADMIN
    public void validarAutorOAdmin(Long actorId, Long autorId, String recurso) {
        UsuarioRemoto actor = usuarioClient.obtenerUsuario(actorId);
        if (!actor.esAdmin() && !actor.id().equals(autorId)) {
            throw new AccesoDenegadoException("Solo el autor o un administrador puede modificar este " + recurso);
        }
    }

    // Solo ADMIN
    public void validarAdmin(Long actorId) {
        if (!usuarioClient.obtenerUsuario(actorId).esAdmin()) {
            throw new AccesoDenegadoException("Esta operacion requiere rol ADMIN");
        }
    }
}
