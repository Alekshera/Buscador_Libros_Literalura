package com.aleshera.desafio.repositorios;

import com.aleshera.desafio.Project.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepositorio extends JpaRepository<Libro, Long> {
}