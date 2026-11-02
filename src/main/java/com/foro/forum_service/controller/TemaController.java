package com.foro.forum_service.controller;

import com.foro.forum_service.dto.ComentarioRequest;
import com.foro.forum_service.dto.ComentarioResponse;
import com.foro.forum_service.dto.TemaRequest;
import com.foro.forum_service.dto.TemaResponse;
import com.foro.forum_service.service.ComentarioService;
import com.foro.forum_service.service.TemaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temas")
public class TemaController {

    private final TemaService temaService;
    private final ComentarioService comentarioService;

    public TemaController(TemaService temaService, ComentarioService comentarioService) {
        this.temaService = temaService;
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public List<TemaResponse> obtenerTodos() {
        return temaService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public TemaResponse obtenerPorId(@PathVariable Long id) {
        return temaService.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<TemaResponse> crear(@Valid @RequestBody TemaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(temaService.crear(request));
    }

    @PutMapping("/{id}")
    public TemaResponse actualizar(@PathVariable Long id, @Valid @RequestBody TemaRequest request) {
        return temaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        temaService.eliminar(id);
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
    public ResponseEntity<ComentarioResponse> comentar(@PathVariable Long id, @Valid @RequestBody ComentarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioService.comentar(id, request));
    }
}
