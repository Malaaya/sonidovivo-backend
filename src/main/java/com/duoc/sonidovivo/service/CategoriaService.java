package com.duoc.sonidovivo.service;

import com.duoc.sonidovivo.dto.CategoriaRequest;
import com.duoc.sonidovivo.dto.CategoriaResponse;
import com.duoc.sonidovivo.exception.ConflictoException;
import com.duoc.sonidovivo.exception.RecursoNoEncontradoException;
import com.duoc.sonidovivo.model.Categoria;
import com.duoc.sonidovivo.repository.CategoriaRepository;
import com.duoc.sonidovivo.repository.ProductoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream().map(CategoriaResponse::desde).toList();
    }

    public CategoriaResponse obtener(Long id) {
        return CategoriaResponse.desde(buscar(id));
    }

    @Transactional
    public CategoriaResponse crear(CategoriaRequest request) {
        String nombre = request.nombre().trim();
        if (categoriaRepository.existsByNombre(nombre)) {
            throw new ConflictoException("Ya existe una categoría con el nombre " + nombre);
        }
        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);
        categoria.setSlug(request.slug().trim());
        return CategoriaResponse.desde(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscar(id);
        String nombre = request.nombre().trim();
        if (categoriaRepository.existsByNombreAndIdNot(nombre, id)) {
            throw new ConflictoException("Ya existe una categoría con el nombre " + nombre);
        }
        categoria.setNombre(nombre);
        categoria.setSlug(request.slug().trim());
        return CategoriaResponse.desde(categoria);
    }

    @Transactional
    public void eliminar(Long id) {
        Categoria categoria = buscar(id);
        if (productoRepository.existsByCategoriaId(id)) {
            throw new ConflictoException("No se puede eliminar la categoría porque tiene productos asociados");
        }
        categoriaRepository.delete(categoria);
        categoriaRepository.flush();
    }

    private Categoria buscar(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con id " + id));
    }
}
