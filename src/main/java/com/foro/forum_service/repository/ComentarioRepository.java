package com.foro.forum_service.repository;

import com.foro.forum_service.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Las consultas publicas excluyen los comentarios baneados por un administrador
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByBaneadoFalse();

    List<Comentario> findByTemaIdAndBaneadoFalseOrderByFechaCreacionAsc(Long temaId);

    List<Comentario> findByUsuarioIdAndBaneadoFalseOrderByFechaCreacionDesc(Long usuarioId);
}
