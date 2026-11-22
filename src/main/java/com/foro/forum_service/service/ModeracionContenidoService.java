package com.foro.forum_service.service;

import com.foro.forum_service.dto.ModeracionRequest;
import com.foro.forum_service.dto.ModeracionResponse;
import com.foro.forum_service.exception.RecursoNoEncontradoException;
import com.foro.forum_service.model.Comentario;
import com.foro.forum_service.model.Tema;
import com.foro.forum_service.repository.ComentarioRepository;
import com.foro.forum_service.repository.TemaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Banea o desbanea temas y comentarios. Solo ADMIN; lo invoca moderation-service al resolver reportes.
@Slf4j
@Service
@Transactional
public class ModeracionContenidoService {

    private final TemaRepository temaRepository;
    private final ComentarioRepository comentarioRepository;
    private final AutorizacionService autorizacionService;

    public ModeracionContenidoService(TemaRepository temaRepository, ComentarioRepository comentarioRepository,
                                      AutorizacionService autorizacionService) {
        this.temaRepository = temaRepository;
        this.comentarioRepository = comentarioRepository;
        this.autorizacionService = autorizacionService;
    }

    public ModeracionResponse moderarTema(Long id, ModeracionRequest request, Long actorId) {
        autorizacionService.validarAdmin(actorId);
        // Aqui se busca sin filtrar baneados, para poder desbanear
        Tema tema = temaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un tema con id " + id));
        tema.setBaneado(request.baneado());
        tema.setMotivoBaneo(request.baneado() ? request.motivo() : null);
        log.info("Tema {}: id={}, por adminId={}", request.baneado() ? "baneado" : "desbaneado", id, actorId);
        return respuesta("TEMA", id, tema.isBaneado(), tema.getMotivoBaneo());
    }

    public ModeracionResponse moderarComentario(Long id, ModeracionRequest request, Long actorId) {
        autorizacionService.validarAdmin(actorId);
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un comentario con id " + id));
        comentario.setBaneado(request.baneado());
        comentario.setMotivoBaneo(request.baneado() ? request.motivo() : null);
        log.info("Comentario {}: id={}, por adminId={}", request.baneado() ? "baneado" : "desbaneado", id, actorId);
        return respuesta("COMENTARIO", id, comentario.isBaneado(), comentario.getMotivoBaneo());
    }

    private ModeracionResponse respuesta(String tipo, Long id, boolean baneado, String motivo) {
        return ModeracionResponse.builder().tipo(tipo).id(id).baneado(baneado).motivo(motivo).build();
    }
}
