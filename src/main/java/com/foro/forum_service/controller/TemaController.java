package com.foro.forum_service.controller;

import com.foro.forum_service.dto.ComentarioRequest;
import com.foro.forum_service.dto.ComentarioResponse;
import com.foro.forum_service.dto.ModeracionRequest;
import com.foro.forum_service.dto.ModeracionResponse;
import com.foro.forum_service.dto.TemaRequest;
import com.foro.forum_service.dto.TemaResponse;
import com.foro.forum_service.service.ComentarioService;
import com.foro.forum_service.service.ModeracionContenidoService;
import com.foro.forum_service.service.TemaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temas")
public class TemaController {

    // Header con el id del usuario que realiza la operacion (el que inicio sesion)
    public static final String USUARIO_HEADER = "X-Usuario-Id";

    private final TemaService temaService;
    private final ComentarioService comentarioService;
    private final ModeracionContenidoService moderacionService;

    public TemaController(TemaService temaService, ComentarioService comentarioService,
                          ModeracionContenidoService moderacionService) {
        this.temaService = temaService;
        this.comentarioService = comentarioService;
        this.moderacionService = moderacionService;
    }

    @GetMapping
    public List<TemaResponse> obtenerTodos() {
        return temaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public TemaResponse obtenerPorId(@PathVariable Long id) {
        return temaService.obtenerPorId(id);
    }

    // Publicar: cualquier usuario registrado; el autor es el del header
    @PostMapping
    public ResponseEntity<TemaResponse> crear(@Valid @RequestBody TemaRequest request,
                                              @RequestHeader(USUARIO_HEADER) Long actorId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(temaService.crear(request, actorId));
    }

    // Editar y eliminar: solo el autor o un ADMIN
    @PutMapping("/{id}")
    public TemaResponse actualizar(@PathVariable Long id, @Valid @RequestBody TemaRequest request,
                                   @RequestHeader(USUARIO_HEADER) Long actorId) {
        return temaService.actualizar(id, request, actorId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestHeader(USUARIO_HEADER) Long actorId) {
        temaService.eliminar(id, actorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<TemaResponse> obtenerPorCategoria(@PathVariable Long categoriaId) {
        return temaService.obtenerPorCategoria(categoriaId);
    }

    @GetMapping("/buscar")
    public List<TemaResponse> buscarPorTitulo(@RequestParam String titulo) {
        return temaService.buscarPorTitulo(titulo);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<TemaResponse> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return temaService.obtenerPorUsuario(usuarioId);
    }

    @GetMapping("/{id}/comentarios")
    public List<ComentarioResponse> obtenerComentarios(@PathVariable Long id) {
        return comentarioService.obtenerPorTema(id);
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ComentarioResponse> comentar(@PathVariable Long id, @Valid @RequestBody ComentarioRequest request,
                                                       @RequestHeader(USUARIO_HEADER) Long actorId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioService.comentar(id, request, actorId));
    }

    // Moderacion (solo ADMIN): banear o desbanear. La usa moderation-service al resolver reportes
    @PatchMapping("/{id}/moderacion")
    public ModeracionResponse moderar(@PathVariable Long id, @Valid @RequestBody ModeracionRequest request,
                                      @RequestHeader(USUARIO_HEADER) Long actorId) {
        return moderacionService.moderarTema(id, request, actorId);
    }
}
