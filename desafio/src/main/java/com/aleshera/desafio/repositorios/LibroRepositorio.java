package com.aleshera.desafio.repositorios;

import com.aleshera.desafio.Project.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepositorio extends JpaRepository<Libro, Long> {
    List<Libro> findByIdiomasContainsIgnoreCase(String idiomaElegido);
}