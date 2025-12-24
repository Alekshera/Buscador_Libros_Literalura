package com.aleshera.desafio.Project;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Es mejor usar un ID numérico para PostgreSQL

    @Column(unique = true)
    private String nombre;

    private String fechaDeNacimiento; // Puedes usar Integer si solo guardas el año
    private String fechaDeDefuncion;

    // Un autor tiene muchos libros, por eso usamos List
    // "autor" es el nombre del campo en la clase Libro
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    // Constructor vacío obligatorio para JPA
    public Autor() {}

    // Constructor para mapear desde DTO (DatosAutor)
    public Autor(DatosAutor d) {
        this.nombre = d.nombre();
        this.fechaDeNacimiento = d.fechaDeNacimiento();
        this.fechaDeDefuncion = d.fechaDeDefuncion();
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getFechaDeDefuncion() {
        return fechaDeDefuncion;
    }

    public void setFechaDeDefuncion(String fechaDeDefuncion) {
        this.fechaDeDefuncion = fechaDeDefuncion;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "Autor: " + nombre +
                " (" + fechaDeNacimiento + " - " + (fechaDeDefuncion == null ? "Presente" : fechaDeDefuncion) + ")";
    }
}