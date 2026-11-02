package com.foro.forum_service.service;

import com.foro.forum_service.dto.TemaRequest;
import com.foro.forum_service.dto.TemaResponse;

import java.util.List;

public interface TemaService {

    List<TemaResponse> obtenerTodos();

    TemaResponse obtenerPorId(Long id);

    TemaResponse crear(TemaRequest request);

    TemaResponse actualizar(Long id, TemaRequest request);

    void eliminar(Long id);

    List<TemaResponse> obtenerPorCategoria(Long categoriaId);

    List<TemaResponse> buscarPorTitulo(String titulo);

    List<TemaResponse> obtenerPorUsuario(Long usuarioId);
}
