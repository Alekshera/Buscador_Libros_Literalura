package com.aleshera.desafio.Project;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne // Muchos libros pertenecen a un solo Autor
    @JoinColumn(name = "autor_id") // Crea una llave foránea en Postgres
    private Autor autor;

    private String idiomas;
    private Double numeroDeDescargas;

    public Libro() {}

    // Constructor actualizado para usar el objeto Autor
    public Libro(DatosLibros datosLibro, Autor autor) {
        this.titulo = datosLibro.titulo();
        this.autor = autor;
        // Si idiomas es una lista en el DTO, lo unimos en un String
        this.idiomas = String.join(", ", datosLibro.idiomas());
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }

    public Libro(String titulo, String nombre, String join, Double aDouble) {
    }

    @Override
    public String toString() {
        return String.format(
                """
                ---------- LIBRO ----------
                Título: %s
                Autor: %s
                Idiomas: %s
                Descargas: %.0f
                ---------------------------
                """,
                titulo,
                (autor != null ? autor.getNombre() : "Desconocido"),
                idiomas,
                numeroDeDescargas
        );
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    public String getIdiomas() { return idiomas; }
    public void setIdiomas(String idiomas) { this.idiomas = idiomas; }

    public Double getNumeroDeDescargas() { return numeroDeDescargas; }
    public void setNumeroDeDescargas(Double numeroDeDescargas) { this.numeroDeDescargas = numeroDeDescargas; }
}