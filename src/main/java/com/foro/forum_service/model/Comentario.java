package com.foro.forum_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "COMENTARIOS")
@Getter
@Setter
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String contenido;

    @Column(nullable = false)
    private Long usuarioId;

    // Al eliminar un tema se eliminan sus comentarios
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TEMA_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Tema tema;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
}
