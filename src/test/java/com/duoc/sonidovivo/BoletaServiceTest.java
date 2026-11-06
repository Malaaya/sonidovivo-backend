package com.duoc.sonidovivo;

import static org.assertj.core.api.Assertions.assertThat;

import com.duoc.sonidovivo.dto.BoletaRequest;
import com.duoc.sonidovivo.dto.DetalleBoletaRequest;
import com.duoc.sonidovivo.model.Categoria;
import com.duoc.sonidovivo.model.Producto;
import com.duoc.sonidovivo.model.Rol;
import com.duoc.sonidovivo.model.Usuario;
import com.duoc.sonidovivo.repository.CategoriaRepository;
import com.duoc.sonidovivo.repository.ProductoRepository;
import com.duoc.sonidovivo.repository.UsuarioRepository;
import com.duoc.sonidovivo.service.BoletaService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BoletaServiceTest {

    @Autowired
    private BoletaService boletaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void crearBoletaDescuentaStockYCalculaTotal() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Guitarras");
        categoria.setSlug("guitarras");
        categoriaRepository.save(categoria);

        Producto guitarra = new Producto();
        guitarra.setCodigo("GTR-001");
        guitarra.setNombre("Guitarra Test");
        guitarra.setMarca("Marca Test");
        guitarra.setModelo("Modelo A");
        guitarra.setDescripcion("Descripcion");
        guitarra.setPrecio(200000);
        guitarra.setStock(5);
        guitarra.setCategoria(categoria);
        productoRepository.save(guitarra);

        Usuario cliente = new Usuario();
        cliente.setRun("11111111-1");
        cliente.setNombre("Cliente Test");
        cliente.setEmail("cliente.test@sonidovivo.cl");
        cliente.setPassword("123456");
        cliente.setRol(Rol.CLIENTE);
        usuarioRepository.save(cliente);

        BoletaRequest request = new BoletaRequest(List.of(
                new DetalleBoletaRequest(guitarra.getId(), 2)
        ));

        var respuesta = boletaService.crear(request, cliente);

        assertThat(respuesta.total()).isEqualTo(400000);
        assertThat(respuesta.items()).hasSize(1);
        assertThat(productoRepository.findById(guitarra.getId()).orElseThrow().getStock()).isEqualTo(3);
    }
}
