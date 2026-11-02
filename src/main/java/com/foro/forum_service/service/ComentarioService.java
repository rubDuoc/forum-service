package com.foro.forum_service.service;

import com.foro.forum_service.dto.ComentarioRequest;
import com.foro.forum_service.dto.ComentarioResponse;

import java.util.List;

public interface ComentarioService {

    List<ComentarioResponse> obtenerTodos();

    ComentarioResponse obtenerPorId(Long id);

    ComentarioResponse comentar(Long temaId, ComentarioRequest request);

    ComentarioResponse actualizar(Long id, ComentarioRequest request);

    void eliminar(Long id);

    List<ComentarioResponse> obtenerPorTema(Long temaId);

    List<ComentarioResponse> obtenerPorUsuario(Long usuarioId);
}
