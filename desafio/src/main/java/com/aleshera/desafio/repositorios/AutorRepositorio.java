package com.aleshera.desafio.repositorios; // Verifica que este nombre coincida con tu carpeta

import com.aleshera.desafio.Project.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;



public interface AutorRepositorio extends JpaRepository<Autor, Long> {
    // Este mtodo es el que te marcaba "Cannot resolve method"
    Optional<Autor> findByNombreContainsIgnoreCase(String nombre);
    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :anio AND (a.fechaDeDefuncion IS NULL OR a.fechaDeDefuncion >= :anio)")
    List<Autor> autoresVivosEnDeterminadoAnio(String anio);
}