package com.aleshera.desafio.principal;

import com.aleshera.desafio.Project.*;
import com.aleshera.desafio.repositorios.AutorRepositorio;
import com.aleshera.desafio.repositorios.LibroRepositorio;
import com.aleshera.desafio.service.ConsumoAPI;
import com.aleshera.desafio.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

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
                3 - Listar autores registrados 
                4 - Listar autores vivos en un año 
                5 - Listar libros por idioma 
                
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
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosPorAnio();
                case 5 -> buscarLibrosPorIdioma();
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



    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepositorio.findAll();

        if (autores.isEmpty()) {
            System.out.println("\n-------------------------------------------");
            System.out.println("No hay autores registrados en la base de datos.");
            System.out.println("-------------------------------------------");
            return;

        }

        System.out.println("\n----------- AUTORES REGISTRADOS -----------");

        autores.forEach(a -> {
            // Obtenemos los títulos de los libros del autor en una sola línea
            // Usamos .stream() para transformar la lista de objetos Libro en una lista de Strings (títulos)
            String librosDelAutor = a.getLibros().stream()
                    .map(Libro::getTitulo)
                    .collect(Collectors.joining(", "));

            // Imprimimos con un formato limpio
            System.out.printf("""
                Autor: %s
                Fecha de Nacimiento: %s
                Fecha de Fallecimiento: %s
                Libros: [%s]
                -------------------------------------------
                """,
                    a.getNombre(),
                    a.getFechaDeNacimiento(),
                    (a.getFechaDeDefuncion() == null ? "N/A" : a.getFechaDeDefuncion()),
                    librosDelAutor
            );
        });
    }private void listarAutoresVivosPorAnio() {
        System.out.println("Ingresa el año para buscar autores que estaban vivos:");
        var anio = teclado.nextLine();

        List<Autor> autoresVivos = autorRepositorio.autoresVivosEnDeterminadoAnio(anio);

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron registros de autores vivos en el año " + anio);
        } else {
            System.out.println("\n--- AUTORES VIVOS EN EL AÑO " + anio + " ---");
            autoresVivos.forEach(a -> {
                System.out.printf("Autor: %s | Nacimiento: %s | Fallecimiento: %s\n",
                        a.getNombre(), a.getFechaDeNacimiento(),
                        (a.getFechaDeDefuncion() == null ? "N/A" : a.getFechaDeDefuncion()));
            });
        }
    }private void buscarLibrosPorIdioma() {
        var menuIdiomas = """
            Ingrese el idioma para buscar los libros:
            es - español
            en - inglés
            fr - francés
            pt - portugués
            """;
        System.out.println(menuIdiomas);
        var idiomaElegido = teclado.nextLine().toLowerCase();

        List<Libro> librosPorIdioma = libroRepositorio.findByIdiomasContainsIgnoreCase(idiomaElegido);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros registrados en el idioma seleccionado (" + idiomaElegido + ").");
        } else {
            System.out.printf("\n--- LIBROS EN EL IDIOMA [%s] ---\n", idiomaElegido.toUpperCase());
            librosPorIdioma.forEach(System.out::println);
        }
    }
    }

   /*

    private void buscarLibrosPorIdioma() {
        System.out.println("Ingrese idioma (es, en, fr, pt):");
        var idioma = teclado.nextLine();
        // Requiere mtodo personalizado en LibroRepository
        // List<Libro> libros = libroRepository.findByIdiomasContains(idioma);
        // libros.forEach(System.out::println);
    }

}*/