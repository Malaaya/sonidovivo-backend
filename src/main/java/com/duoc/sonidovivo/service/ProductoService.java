package com.duoc.sonidovivo.service;

import com.duoc.sonidovivo.dto.ProductoRequest;
import com.duoc.sonidovivo.dto.ProductoResponse;
import com.duoc.sonidovivo.exception.ApiException;
import com.duoc.sonidovivo.model.Categoria;
import com.duoc.sonidovivo.model.Producto;
import com.duoc.sonidovivo.repository.CategoriaRepository;
import com.duoc.sonidovivo.repository.ProductoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<ProductoResponse> listar(Long categoriaId) {
        List<Producto> productos = categoriaId == null
                ? productoRepository.findAll()
                : productoRepository.findByCategoriaId(categoriaId);
        return productos.stream().map(ProductoResponse::desde).toList();
    }

    public ProductoResponse obtener(Long id) {
        return ProductoResponse.desde(buscar(id));
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        String codigo = request.codigo().trim();
        if (productoRepository.existsByCodigo(codigo)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe un producto con el código " + codigo);
        }
        Producto producto = new Producto();
        aplicar(producto, request);
        return ProductoResponse.desde(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = buscar(id);
        String codigo = request.codigo().trim();
        if (productoRepository.existsByCodigoAndIdNot(codigo, id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Ya existe otro producto con el código " + codigo);
        }
        aplicar(producto, request);
        return ProductoResponse.desde(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = buscar(id);
        productoRepository.delete(producto);
        productoRepository.flush();
    }

    private void aplicar(Producto producto, ProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "La categoría indicada no existe"));
        producto.setCodigo(request.codigo().trim());
        producto.setNombre(request.nombre().trim());
        producto.setMarca(request.marca().trim());
        producto.setModelo(request.modelo());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setImagen(request.imagen());
        producto.setCategoria(categoria);
    }

    private Producto buscar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "Producto no encontrado con id " + id));
    }
}
