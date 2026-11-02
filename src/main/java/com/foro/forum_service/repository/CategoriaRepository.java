package com.foro.forum_service.repository;

import com.foro.forum_service.dto.CategoriaResumen;
import com.foro.forum_service.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    @Query("""
            select new com.foro.forum_service.dto.CategoriaResumen(c.id, c.nombre, count(t))
            from Categoria c left join Tema t on t.categoria = c
            group by c.id, c.nombre
            order by count(t) desc, c.nombre
            """)
    List<CategoriaResumen> resumenTemasPorCategoria();
}
