package com.foro.forum_service.service;

import com.foro.forum_service.dto.ComentarioRequest;
import com.foro.forum_service.dto.ComentarioResponse;
import com.foro.forum_service.exception.RecursoNoEncontradoException;
import com.foro.forum_service.model.Comentario;
import com.foro.forum_service.model.Tema;
import com.foro.forum_service.repository.ComentarioRepository;
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
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TemaRepository temaRepository;
    private final AutorizacionService autorizacionService;

    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, TemaRepository temaRepository,
                                 AutorizacionService autorizacionService) {
        this.comentarioRepository = comentarioRepository;
        this.temaRepository = temaRepository;
        this.autorizacionService = autorizacionService;
    }

    @Override
    public List<ComentarioResponse> obtenerTodos() {
        return comentarioRepository.findByBaneadoFalse().stream().map(ComentarioResponse::from).toList();
    }

    @Override
    public ComentarioResponse obtenerPorId(Long id) {
        return ComentarioResponse.from(buscarComentario(id));
    }

    @Override
    public ComentarioResponse comentar(Long temaId, ComentarioRequest request, Long actorId) {
        Tema tema = buscarTema(temaId);
        autorizacionService.validarUsuario(actorId);
        Comentario comentario = new Comentario();
        comentario.setTema(tema);
        comentario.setContenido(request.contenido());
        comentario.setUsuarioId(actorId);
        comentario.setFechaCreacion(LocalDateTime.now());
        Comentario guardado = comentarioRepository.save(comentario);
        log.info("Comentario publicado: id={}, temaId={}, usuarioId={}", guardado.getId(), temaId, actorId);
        return ComentarioResponse.from(guardado);
    }

    @Override
    public ComentarioResponse actualizar(Long id, ComentarioRequest request, Long actorId) {
        Comentario comentario = buscarComentario(id);
        autorizacionService.validarAutorOAdmin(actorId, comentario.getUsuarioId(), "comentario");
        comentario.setContenido(request.contenido());
        log.info("Comentario actualizado: id={}, por actorId={}", id, actorId);
        return ComentarioResponse.from(comentarioRepository.save(comentario));
    }

    @Override
    public void eliminar(Long id, Long actorId) {
        Comentario comentario = buscarComentario(id);
        autorizacionService.validarAutorOAdmin(actorId, comentario.getUsuarioId(), "comentario");
        comentarioRepository.delete(comentario);
        log.info("Comentario eliminado: id={}, por actorId={}", id, actorId);
    }

    @Override
    public List<ComentarioResponse> obtenerPorTema(Long temaId) {
        buscarTema(temaId);
        return comentarioRepository.findByTemaIdAndBaneadoFalseOrderByFechaCreacionAsc(temaId).stream().map(ComentarioResponse::from).toList();
    }

    @Override
    public List<ComentarioResponse> obtenerPorUsuario(Long usuarioId) {
        return comentarioRepository.findByUsuarioIdAndBaneadoFalseOrderByFechaCreacionDesc(usuarioId).stream().map(ComentarioResponse::from).toList();
    }

    private Tema buscarTema(Long temaId) {
        return temaRepository.findById(temaId)
                .filter(tema -> !tema.isBaneado())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un tema con id " + temaId));
    }

    private Comentario buscarComentario(Long id) {
        return comentarioRepository.findById(id)
                .filter(comentario -> !comentario.isBaneado())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un comentario con id " + id));
    }
}
