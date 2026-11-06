package com.duoc.sonidovivo.repository;

import com.duoc.sonidovivo.model.Boleta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {

    List<Boleta> findByUsuarioIdOrderByFechaDesc(Long usuarioId);

    List<Boleta> findAllByOrderByFechaDesc();

    boolean existsByNumero(String numero);
}
