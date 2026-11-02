package com.foro.forum_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TEMAS")
@Getter
@Setter
@NoArgsConstructor
public class Tema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, length = 2000)
    private String contenido;

    // Id del autor en user-service (cada microservicio maneja sus propias tablas)
    @Column(nullable = false)
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CATEGORIA_ID", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}
