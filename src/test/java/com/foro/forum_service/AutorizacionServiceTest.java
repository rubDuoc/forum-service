package com.foro.forum_service;

import com.foro.forum_service.client.UsuarioClient;
import com.foro.forum_service.client.UsuarioClient.UsuarioRemoto;
import com.foro.forum_service.exception.AccesoDenegadoException;
import com.foro.forum_service.service.AutorizacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// Reglas de privilegios del foro (user-service simulado con un mock)
class AutorizacionServiceTest {

    private AutorizacionService autorizacion;

    @BeforeEach
    void preparar() {
        UsuarioClient usuarioClient = mock(UsuarioClient.class);
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(new UsuarioRemoto(1L, "admin_foro", "ADMIN"));
        when(usuarioClient.obtenerUsuario(4L)).thenReturn(new UsuarioRemoto(4L, "juan_gamer", "USUARIO"));
        when(usuarioClient.obtenerUsuario(999L))
                .thenThrow(new AccesoDenegadoException("El usuario 999 que realiza la operacion no existe"));
        autorizacion = new AutorizacionService(usuarioClient);
    }

    @Test
    void elAutorPuedeModificarSuContenido() {
        assertThatCode(() -> autorizacion.validarAutorOAdmin(4L, 4L, "tema")).doesNotThrowAnyException();
    }

    @Test
    void unUsuarioNoPuedeModificarContenidoAjeno() {
        assertThatThrownBy(() -> autorizacion.validarAutorOAdmin(4L, 3L, "tema"))
                .isInstanceOf(AccesoDenegadoException.class);
    }

    @Test
    void elAdminPuedeModificarCualquierContenido() {
        assertThatCode(() -> autorizacion.validarAutorOAdmin(1L, 3L, "comentario")).doesNotThrowAnyException();
    }

    @Test
    void soloElAdminGestionaCategorias() {
        assertThatCode(() -> autorizacion.validarAdmin(1L)).doesNotThrowAnyException();
        assertThatThrownBy(() -> autorizacion.validarAdmin(4L)).isInstanceOf(AccesoDenegadoException.class);
    }

    @Test
    void unUsuarioInexistenteNoPuedePublicar() {
        assertThatThrownBy(() -> autorizacion.validarUsuario(999L)).isInstanceOf(AccesoDenegadoException.class);
    }
}
