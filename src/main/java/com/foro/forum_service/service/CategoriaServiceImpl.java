package com.foro.forum_service.service;

import com.foro.forum_service.dto.CategoriaRequest;
import com.foro.forum_service.dto.CategoriaResumen;
import com.foro.forum_service.exception.ConflictoException;
import com.foro.forum_service.exception.RecursoNoEncontradoException;
import com.foro.forum_service.model.Categoria;
import com.foro.forum_service.repository.CategoriaRepository;
import com.foro.forum_service.repository.TemaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
// Transaccional: cada operacion trabaja dentro de una transaccion (necesario con open-in-view=false)
@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final TemaRepository temaRepository;
    private final AutorizacionService autorizacionService;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, TemaRepository temaRepository,
                                AutorizacionService autorizacionService) {
        this.categoriaRepository = categoriaRepository;
        this.temaRepository = temaRepository;
        this.autorizacionService = autorizacionService;
    }

    @Override
    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una categoria con id " + id));
    }

    @Override
    public Categoria crear(CategoriaRequest request, Long actorId) {
        autorizacionService.validarAdmin(actorId);
        if (categoriaRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new ConflictoException("Ya existe la categoria '" + request.nombre() + "'");
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoria creada: id={}, nombre={}", guardada.getId(), guardada.getNombre());
        return guardada;
    }

    @Override
    public Categoria actualizar(Long id, CategoriaRequest request, Long actorId) {
        autorizacionService.validarAdmin(actorId);
        Categoria categoria = obtenerPorId(id);
        if (!categoria.getNombre().equalsIgnoreCase(request.nombre())
                && categoriaRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new ConflictoException("Ya existe la categoria '" + request.nombre() + "'");
        }
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        log.info("Categoria actualizada: id={}", id);
        return categoriaRepository.save(categoria);
    }

    @Override
    public void eliminar(Long id, Long actorId) {
        autorizacionService.validarAdmin(actorId);
        Categoria categoria = obtenerPorId(id);
        if (temaRepository.existsByCategoriaId(id)) {
            throw new ConflictoException("No se puede eliminar la categoria porque tiene temas asociados");
        }
        categoriaRepository.delete(categoria);
        log.info("Categoria eliminada: id={}", id);
    }

    @Override
    public List<CategoriaResumen> resumen() {
        return categoriaRepository.resumenTemasPorCategoria();
    }
}
