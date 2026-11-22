package com.foro.forum_service.service;

import com.foro.forum_service.dto.CategoriaRequest;
import com.foro.forum_service.dto.CategoriaResumen;
import com.foro.forum_service.model.Categoria;

import java.util.List;

public interface CategoriaService {

    List<Categoria> obtenerTodas();

    Categoria obtenerPorId(Long id);

    Categoria crear(CategoriaRequest request, Long actorId);

    Categoria actualizar(Long id, CategoriaRequest request, Long actorId);

    void eliminar(Long id, Long actorId);

    List<CategoriaResumen> resumen();
}
