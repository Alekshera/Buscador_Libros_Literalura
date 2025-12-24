package com.aleshera.desafio;

import com.aleshera.desafio.principal.Principal;
import com.aleshera.desafio.repositorios.AutorRepositorio;
import com.aleshera.desafio.repositorios.LibroRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DesafioApplication implements CommandLineRunner {

    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;

    public static void main(String[] args) {
        SpringApplication.run(DesafioApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // AQUÍ ESTÁ EL TRUCO: Pasamos los repositorios al constructor
        Principal principal = new Principal(libroRepositorio, autorRepositorio);
        principal.muestraElMenu();
    }
}
