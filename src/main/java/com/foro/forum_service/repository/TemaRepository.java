package com.foro.forum_service.repository;

import com.foro.forum_service.model.Tema;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Las consultas publicas excluyen los temas baneados por un administrador
public interface TemaRepository extends JpaRepository<Tema, Long> {

    @EntityGraph(attributePaths = "categoria")
    List<Tema> findByBaneadoFalseOrderByFechaCreacionDesc();

    @EntityGraph(attributePaths = "categoria")
    List<Tema> findByCategoriaIdAndBaneadoFalseOrderByFechaCreacionDesc(Long categoriaId);

    @EntityGraph(attributePaths = "categoria")
    List<Tema> findByTituloContainingIgnoreCaseAndBaneadoFalseOrderByFechaCreacionDesc(String titulo);

    @EntityGraph(attributePaths = "categoria")
    List<Tema> findByUsuarioIdAndBaneadoFalseOrderByFechaCreacionDesc(Long usuarioId);

    boolean existsByCategoriaId(Long categoriaId);
}
