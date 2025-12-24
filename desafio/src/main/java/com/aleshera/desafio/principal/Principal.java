package com.aleshera.desafio.principal;

import com.aleshera.desafio.Project.*;
import com.aleshera.desafio.repositorios.AutorRepositorio;
import com.aleshera.desafio.repositorios.LibroRepositorio;
import com.aleshera.desafio.service.ConsumoAPI;
import com.aleshera.desafio.service.ConvierteDatos;

import java.util.*;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    private LibroRepositorio libroRepositorio;
    private AutorRepositorio autorRepositorio;

    public Principal(LibroRepositorio libroRepository, AutorRepositorio autorRepository) {
        this.libroRepositorio = libroRepository;
        this.autorRepositorio = autorRepository;
    }

    public Principal() {

    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                --------------------------------------------
                ELIJA LA OPCIÓN A TRAVÉS DE SU NÚMERO:
                1 - Buscar libro por título 
                2 - Listar libros registrados
                3 - Listar autores registrados (Próximamente)
                4 - Listar autores vivos en un año (Próximamente)
                5 - Listar libros por idioma (Próximamente)
                
                0 - Salir
                --------------------------------------------
                """;
            System.out.println(menu);
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un número válido.");
                teclado.nextLine();
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibroPorTitulo();
                case 2 -> listarLibrosRegistrados();
                case 3 -> System.out.println("Funcionalidad en desarrollo...");
                case 4 -> System.out.println("Funcionalidad en desarrollo...");
                case 5 -> System.out.println("Funcionalidad en desarrollo...");
                case 0 -> System.out.println("Cerrando la aplicación...");
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibros datosLibro = libroBuscado.get();

            // Lógica de persistencia básica
            DatosAutor datosAutor = datosLibro.autores().get(0);

            // Buscamos si el autor ya existe para no duplicar
            Autor autor = autorRepositorio.findByNombreContainsIgnoreCase(datosAutor.nombre())
                    .orElseGet(() -> autorRepositorio.save(new Autor(datosAutor)));

            try {
                Libro libro = new Libro(datosLibro, autor);
                libroRepositorio.save(libro);
                System.out.println(libro);
            } catch (Exception e) {
                System.out.println("Error: El libro ya está registrado en la base de datos.");
            }
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepositorio.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            libros.forEach(System.out::println);
        }
    }

    /* MÉTODOS COMENTADOS PARA EVITAR ERRORES DE COMPILACIÓN
    HASTA QUE LOS REPOSITORIOS ESTÉN LISTOS:

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosPorAnio() {
        System.out.println("Ingrese el año:");
        var anio = teclado.nextInt();
        // Requiere mtodo personalizado en AutorRepository
        // List<Autor> autores = autorRepository.autoresVivosEnDeterminadoAnio(anio);
        // autores.forEach(System.out::println);
    }

    private void buscarLibrosPorIdioma() {
        System.out.println("Ingrese idioma (es, en, fr, pt):");
        var idioma = teclado.nextLine();
        // Requiere mtodo personalizado en LibroRepository
        // List<Libro> libros = libroRepository.findByIdiomasContains(idioma);
        // libros.forEach(System.out::println);
    }
    */
}