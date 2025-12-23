package com.aleshera.desafio.Project;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties (ignoreUnknown = true)

public record Datos(
        //@JsonAlias("count") Integer count,
        @JsonAlias("results") List<DatosLibros> resultados
) {
}
