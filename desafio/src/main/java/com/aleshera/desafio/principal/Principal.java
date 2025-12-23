package com.aleshera.desafio.principal;

import com.aleshera.desafio.Project.Datos;
import com.aleshera.desafio.Project.DatosLibros;
import com.aleshera.desafio.Project.Libros;
import com.aleshera.desafio.service.ConsumoAPI;
import com.aleshera.desafio.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    // Lista para almacenar los libros buscados
    private List<DatosLibros> librosBuscados = new ArrayList<>();

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            var menu = """
                Escoge una opción:
                
                1 - Buscar libro por titulo
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Buscar autores vivos en determinado año
                5 - Buscar libros por idioma
                              
                0 - Salir
                """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    System.out.println("Funcionalidad no implementada");
                    break;
                case 4:
                    System.out.println("Funcionalidad no implementada");
                    break;
                case 5:
                    System.out.println("Funcionalidad no implementada");
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingresa el nombre del libro:");
        var tituloLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();

        if(libroBuscado.isPresent()){
            System.out.println("Libro Encontrado");
            System.out.println(libroBuscado.get());
            // Guardar el libro en la lista de libros buscados
            librosBuscados.add(libroBuscado.get());
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listarLibrosRegistrados() {
        if (librosBuscados.isEmpty()) {
            System.out.println("No hay libros registrados aún.");
            return;
        }

        System.out.println("Libros registrados:");
        System.out.println("===================");

        // Convertir DatosLibros a objetos Libros para mostrar
        List<Libros> libros = librosBuscados.stream()
                .map(d -> new Libros(d.titulo(),
                        d.autores().get(0).nombre(), // Asumiendo que hay al menos un autor
                        String.join(", ", d.idiomas()),
                        d.numeroDeDescargas()))
                .collect(Collectors.toList());

        libros.forEach(System.out::println);
    }
}