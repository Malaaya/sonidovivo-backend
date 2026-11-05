package com.duoc.sonidovivo.repository;

import com.duoc.sonidovivo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombre(String nombre);
}