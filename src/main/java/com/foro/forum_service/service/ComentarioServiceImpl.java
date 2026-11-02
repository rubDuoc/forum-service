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

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TemaRepository temaRepository;

    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, TemaRepository temaRepository) {
        this.comentarioRepository = comentarioRepository;
        this.temaRepository = temaRepository;
    }

    @Override
    public List<ComentarioResponse> obtenerTodos() {
        return comentarioRepository.findAll().stream().map(ComentarioResponse::from).toList();
    }

    @Override
    public ComentarioResponse obtenerPorId(Long id) {
        return ComentarioResponse.from(buscarComentario(id));
    }

    @Override
    public ComentarioResponse comentar(Long temaId, ComentarioRequest request) {
        Comentario comentario = new Comentario();
        comentario.setTema(buscarTema(temaId));
        comentario.setContenido(request.contenido());
        comentario.setUsuarioId(request.usuarioId());
        comentario.setFechaCreacion(LocalDateTime.now());
        Comentario guardado = comentarioRepository.save(comentario);
        log.info("Comentario publicado: id={}, temaId={}, usuarioId={}", guardado.getId(), temaId, request.usuarioId());
        return ComentarioResponse.from(guardado);
    }

    @Override
    public ComentarioResponse actualizar(Long id, ComentarioRequest request) {
        Comentario comentario = buscarComentario(id);
        comentario.setContenido(request.contenido());
        comentario.setUsuarioId(request.usuarioId());
        log.info("Comentario actualizado: id={}", id);
        return ComentarioResponse.from(comentarioRepository.save(comentario));
    }

    @Override
    public void eliminar(Long id) {
        comentarioRepository.delete(buscarComentario(id));
        log.info("Comentario eliminado: id={}", id);
    }

    @Override
    public List<ComentarioResponse> obtenerPorTema(Long temaId) {
        buscarTema(temaId);
        return comentarioRepository.findByTemaIdOrderByFechaCreacionAsc(temaId).stream().map(ComentarioResponse::from).toList();
    }

    @Override
    public List<ComentarioResponse> obtenerPorUsuario(Long usuarioId) {
        return comentarioRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId).stream().map(ComentarioResponse::from).toList();
    }

    private Tema buscarTema(Long temaId) {
        return temaRepository.findById(temaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un tema con id " + temaId));
    }

    private Comentario buscarComentario(Long id) {
        return comentarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un comentario con id " + id));
    }
}
