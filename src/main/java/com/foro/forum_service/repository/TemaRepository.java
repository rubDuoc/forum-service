package com.foro.forum_service.repository;

import com.foro.forum_service.model.Tema;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemaRepository extends JpaRepository<Tema, Long> {

    @EntityGraph(attributePaths = "categoria")
    List<Tema> findAllByOrderByFechaCreacionDesc();

    @EntityGraph(attributePaths = "categoria")
    List<Tema> findByCategoriaIdOrderByFechaCreacionDesc(Long categoriaId);

    @EntityGraph(attributePaths = "categoria")
    List<Tema> findByTituloContainingIgnoreCaseOrderByFechaCreacionDesc(String titulo);

    @EntityGraph(attributePaths = "categoria")
    List<Tema> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    boolean existsByCategoriaId(Long categoriaId);
}
