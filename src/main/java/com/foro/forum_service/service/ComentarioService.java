package com.foro.forum_service.service;

import com.foro.forum_service.dto.ComentarioRequest;
import com.foro.forum_service.dto.ComentarioResponse;

import java.util.List;

public interface ComentarioService {

    List<ComentarioResponse> obtenerTodos();

    ComentarioResponse obtenerPorId(Long id);

    ComentarioResponse comentar(Long temaId, ComentarioRequest request, Long actorId);

    ComentarioResponse actualizar(Long id, ComentarioRequest request, Long actorId);

    void eliminar(Long id, Long actorId);

    List<ComentarioResponse> obtenerPorTema(Long temaId);

    List<ComentarioResponse> obtenerPorUsuario(Long usuarioId);
}
