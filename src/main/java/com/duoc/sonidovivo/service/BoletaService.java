package com.duoc.sonidovivo.service;

import com.duoc.sonidovivo.dto.BoletaRequest;
import com.duoc.sonidovivo.dto.BoletaResponse;
import com.duoc.sonidovivo.dto.DetalleBoletaRequest;
import com.duoc.sonidovivo.exception.ApiException;
import com.duoc.sonidovivo.model.Boleta;
import com.duoc.sonidovivo.model.DetalleBoleta;
import com.duoc.sonidovivo.model.Producto;
import com.duoc.sonidovivo.model.Rol;
import com.duoc.sonidovivo.model.Usuario;
import com.duoc.sonidovivo.repository.BoletaRepository;
import com.duoc.sonidovivo.repository.ProductoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoletaService {

    private final BoletaRepository boletaRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public BoletaResponse crear(BoletaRequest request, Usuario usuario) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "La compra debe incluir al menos un producto");
        }

        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);
        boleta.setFecha(LocalDateTime.now());
        boleta.setNumero(generarNumero());
        boleta.setDetalles(new ArrayList<>());

        int total = 0;

        for (DetalleBoletaRequest item : request.items()) {
            Producto producto = productoRepository.findById(item.productoId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                            "El producto con id " + item.productoId() + " no existe"));

            if (producto.getStock() < item.cantidad()) {
                throw new ApiException(HttpStatus.CONFLICT,
                        "No hay stock suficiente para el producto " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.cantidad());
            productoRepository.save(producto);

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setBoleta(boleta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            boleta.getDetalles().add(detalle);
            total += detalle.getSubtotal();
        }

        boleta.setTotal(total);
        Boleta guardada = boletaRepository.save(boleta);
        return BoletaResponse.desde(guardada);
    }

    public List<BoletaResponse> listar() {
        return boletaRepository.findAllByOrderByFechaDesc().stream()
                .map(BoletaResponse::desde)
                .toList();
    }

    public List<BoletaResponse> misCompras(Usuario usuario) {
        return boletaRepository.findByUsuarioIdOrderByFechaDesc(usuario.getId()).stream()
                .map(BoletaResponse::desde)
                .toList();
    }

    public BoletaResponse obtener(Long id, Usuario usuario) {
        Boleta boleta = boletaRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "Boleta no encontrada con id " + id));

        if (usuario.getRol() == Rol.CLIENTE && !boleta.getUsuario().getId().equals(usuario.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                    "No tienes permisos para ver esta boleta");
        }

        return BoletaResponse.desde(boleta);
    }

    private String generarNumero() {
        String numero;
        do {
            numero = "BV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (boletaRepository.existsByNumero(numero));
        return numero;
    }
}
