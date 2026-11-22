package com.foro.forum_service.controller;

import com.foro.forum_service.dto.CategoriaRequest;
import com.foro.forum_service.dto.CategoriaResumen;
import com.foro.forum_service.model.Categoria;
import com.foro.forum_service.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    // Header con el id del usuario que realiza la operacion (el que inicio sesion)
    public static final String USUARIO_HEADER = "X-Usuario-Id";

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> obtenerTodas() {
        return categoriaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public Categoria obtenerPorId(@PathVariable Long id) {
        return categoriaService.obtenerPorId(id);
    }

    // Gestion de categorias: solo ADMIN
    @PostMapping
    public ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaRequest request,
                                           @RequestHeader(USUARIO_HEADER) Long actorId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.crear(request, actorId));
    }

    @PutMapping("/{id}")
    public Categoria actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest request,
                                @RequestHeader(USUARIO_HEADER) Long actorId) {
        return categoriaService.actualizar(id, request, actorId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, @RequestHeader(USUARIO_HEADER) Long actorId) {
        categoriaService.eliminar(id, actorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumen")
    public List<CategoriaResumen> resumen() {
        return categoriaService.resumen();
    }
}
