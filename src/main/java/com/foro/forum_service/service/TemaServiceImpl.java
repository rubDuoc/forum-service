package com.foro.forum_service.service;

import com.foro.forum_service.dto.TemaRequest;
import com.foro.forum_service.dto.TemaResponse;
import com.foro.forum_service.exception.RecursoNoEncontradoException;
import com.foro.forum_service.model.Tema;
import com.foro.forum_service.repository.TemaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TemaServiceImpl implements TemaService {

    private final TemaRepository temaRepository;
    private final CategoriaService categoriaService;

    public TemaServiceImpl(TemaRepository temaRepository, CategoriaService categoriaService) {
        this.temaRepository = temaRepository;
        this.categoriaService = categoriaService;
    }

    @Override
    public List<TemaResponse> obtenerTodos() {
        return temaRepository.findAllByOrderByFechaCreacionDesc().stream().map(TemaResponse::from).toList();
    }

    @Override
    public TemaResponse obtenerPorId(Long id) {
        return TemaResponse.from(buscarTema(id));
    }

    @Override
    public TemaResponse crear(TemaRequest request) {
        Tema tema = new Tema();
        copiarDatos(request, tema);
        tema.setFechaCreacion(LocalDateTime.now());
        Tema guardado = temaRepository.save(tema);
        log.info("Tema publicado: id={}, usuarioId={}, categoria={}",
                guardado.getId(), guardado.getUsuarioId(), guardado.getCategoria().getNombre());
        return TemaResponse.from(guardado);
    }

    @Override
    public TemaResponse actualizar(Long id, TemaRequest request) {
        Tema tema = buscarTema(id);
        copiarDatos(request, tema);
        log.info("Tema actualizado: id={}", id);
        return TemaResponse.from(temaRepository.save(tema));
    }

    @Override
    public void eliminar(Long id) {
        temaRepository.delete(buscarTema(id));
        log.info("Tema eliminado junto a sus comentarios: id={}", id);
    }

    @Override
    public List<TemaResponse> obtenerPorCategoria(Long categoriaId) {
        categoriaService.obtenerPorId(categoriaId);
        return temaRepository.findByCategoriaIdOrderByFechaCreacionDesc(categoriaId).stream().map(TemaResponse::from).toList();
    }

    @Override
    public List<TemaResponse> buscarPorTitulo(String titulo) {
        return temaRepository.findByTituloContainingIgnoreCaseOrderByFechaCreacionDesc(titulo).stream().map(TemaResponse::from).toList();
    }

    @Override
    public List<TemaResponse> obtenerPorUsuario(Long usuarioId) {
        return temaRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId).stream().map(TemaResponse::from).toList();
    }

    private Tema buscarTema(Long id) {
        return temaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un tema con id " + id));
    }

    private void copiarDatos(TemaRequest request, Tema tema) {
        tema.setTitulo(request.titulo());
        tema.setContenido(request.contenido());
        tema.setUsuarioId(request.usuarioId());
        tema.setCategoria(categoriaService.obtenerPorId(request.categoriaId()));
    }
}
