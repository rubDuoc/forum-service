package com.foro.forum_service.repository;

import com.foro.forum_service.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByTemaIdOrderByFechaCreacionAsc(Long temaId);

    List<Comentario> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);
}
