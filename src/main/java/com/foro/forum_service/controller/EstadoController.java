package com.foro.forum_service.controller;

import com.foro.forum_service.dto.EstadoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

// Endpoint de salud: permite comprobar que el microservicio esta arriba (util en el Cloud)
@RestController
@RequestMapping("/api/estado")
public class EstadoController {

    @Value("${spring.application.name}")
    private String servicio;

    @GetMapping
    public EstadoResponse estado() {
        return EstadoResponse.builder()
                .servicio(servicio)
                .estado("UP")
                .fecha(LocalDateTime.now())
                .build();
    }
}
