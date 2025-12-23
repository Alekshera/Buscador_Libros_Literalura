package com.aleshera.desafio.Project;

public class Libros {
    private String titulo;
    private String autor;
    private String idiomas;
    private Double numeroDeDescargas;

    // Constructor que acepta parámetros
    public Libros(String titulo, String autor, String idiomas, Double numeroDeDescargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idiomas = idiomas;
        this.numeroDeDescargas = numeroDeDescargas;
    }

    // Constructor que acepta DatosLibros (opcional pero útil)
    public Libros(DatosLibros datosLibro) {
        this.titulo = datosLibro.titulo();
        this.autor = datosLibro.autores().get(0).nombre(); // Asume al menos un autor
        this.idiomas = String.join(", ", datosLibro.idiomas());
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();
    }

    @Override
    public String toString() {
        return String.format(
                """
                ============================================
                Título: %s
                Autor: %s
                Idiomas: %s
                Número de descargas: %.0f
                ============================================
                """,
                titulo,
                autor,
                idiomas,
                numeroDeDescargas
        );
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }
}