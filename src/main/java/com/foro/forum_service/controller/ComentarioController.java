package com.foro.forum_service.controller;

import com.foro.forum_service.dto.ComentarioRequest;
import com.foro.forum_service.dto.ComentarioResponse;
import com.foro.forum_service.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Los comentarios se crean desde POST /api/temas/{id}/comentarios
@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    // Header con el id del usuario que realiza la operacion (el que inicio sesion)
    public static final String USUARIO_HEADER = "X-Usuario-Id";

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public List<ComentarioResponse> obtenerTodos() {
        return comentarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ComentarioResponse obtenerPorId(@PathVariable Long id) {
        return comentarioService.obtenerPorId(id);
    }

    // Editar y eliminar: solo el autor o un ADMIN
    @PutMapping("/{id}")
    public ComentarioResponse actualizar(@PathVariable Long id, @Valid @RequestBody ComentarioRequest request,
                                         @RequestHeader(USUARIO_HEADER) Long actorId) {
        return comentarioService.actualizar(id, request, actorId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestHeader(USUARIO_HEADER) Long actorId) {
        comentarioService.eliminar(id, actorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<ComentarioResponse> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return comentarioService.obtenerPorUsuario(usuarioId);
    }
}
