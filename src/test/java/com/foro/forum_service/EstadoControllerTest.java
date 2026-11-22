package com.foro.forum_service;

import com.foro.forum_service.controller.EstadoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Prueba de la capa web sin base de datos: el endpoint de estado responde UP
@WebMvcTest(EstadoController.class)
@TestPropertySource(properties = "cors.origenes-permitidos=http://localhost:4200")
class EstadoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void estadoRespondeUp() throws Exception {
        mvc.perform(get("/api/estado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("UP"));
    }
}
