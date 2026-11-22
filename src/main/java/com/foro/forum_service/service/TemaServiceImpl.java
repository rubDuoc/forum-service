package com.foro.forum_service.service;

import com.foro.forum_service.dto.TemaRequest;
import com.foro.forum_service.dto.TemaResponse;
import com.foro.forum_service.exception.RecursoNoEncontradoException;
import com.foro.forum_service.model.Tema;
import com.foro.forum_service.repository.TemaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
// Transaccional: cada operacion trabaja dentro de una transaccion (necesario con open-in-view=false)
@Service
@Transactional
public class TemaServiceImpl implements TemaService {

    private final TemaRepository temaRepository;
    private final CategoriaService categoriaService;
    private final AutorizacionService autorizacionService;

    public TemaServiceImpl(TemaRepository temaRepository, CategoriaService categoriaService,
                           AutorizacionService autorizacionService) {
        this.temaRepository = temaRepository;
        this.categoriaService = categoriaService;
        this.autorizacionService = autorizacionService;
    }

    @Override
    public List<TemaResponse> obtenerTodos() {
        return temaRepository.findByBaneadoFalseOrderByFechaCreacionDesc().stream().map(TemaResponse::from).toList();
    }

    @Override
    public TemaResponse obtenerPorId(Long id) {
        return TemaResponse.from(buscarTema(id));
    }

    @Override
    public TemaResponse crear(TemaRequest request, Long actorId) {
        autorizacionService.validarUsuario(actorId);
        Tema tema = new Tema();
        tema.setUsuarioId(actorId);
        copiarDatos(request, tema);
        tema.setFechaCreacion(LocalDateTime.now());
        Tema guardado = temaRepository.save(tema);
        log.info("Tema publicado: id={}, usuarioId={}, categoria={}",
                guardado.getId(), guardado.getUsuarioId(), guardado.getCategoria().getNombre());
        return TemaResponse.from(guardado);
    }

    @Override
    public TemaResponse actualizar(Long id, TemaRequest request, Long actorId) {
        Tema tema = buscarTema(id);
        autorizacionService.validarAutorOAdmin(actorId, tema.getUsuarioId(), "tema");
        copiarDatos(request, tema);
        log.info("Tema actualizado: id={}, por actorId={}", id, actorId);
        return TemaResponse.from(temaRepository.save(tema));
    }

    @Override
    public void eliminar(Long id, Long actorId) {
        Tema tema = buscarTema(id);
        autorizacionService.validarAutorOAdmin(actorId, tema.getUsuarioId(), "tema");
        temaRepository.delete(tema);
        log.info("Tema eliminado junto a sus comentarios: id={}, por actorId={}", id, actorId);
    }

    @Override
    public List<TemaResponse> obtenerPorCategoria(Long categoriaId) {
        categoriaService.obtenerPorId(categoriaId);
        return temaRepository.findByCategoriaIdAndBaneadoFalseOrderByFechaCreacionDesc(categoriaId).stream().map(TemaResponse::from).toList();
    }

    @Override
    public List<TemaResponse> buscarPorTitulo(String titulo) {
        return temaRepository.findByTituloContainingIgnoreCaseAndBaneadoFalseOrderByFechaCreacionDesc(titulo).stream().map(TemaResponse::from).toList();
    }

    @Override
    public List<TemaResponse> obtenerPorUsuario(Long usuarioId) {
        return temaRepository.findByUsuarioIdAndBaneadoFalseOrderByFechaCreacionDesc(usuarioId).stream().map(TemaResponse::from).toList();
    }

    private Tema buscarTema(Long id) {
        // Un tema baneado se comporta como inexistente para el resto del foro
        return temaRepository.findById(id)
                .filter(tema -> !tema.isBaneado())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un tema con id " + id));
    }

    private void copiarDatos(TemaRequest request, Tema tema) {
        tema.setTitulo(request.titulo());
        tema.setContenido(request.contenido());
        tema.setCategoria(categoriaService.obtenerPorId(request.categoriaId()));
    }
}
