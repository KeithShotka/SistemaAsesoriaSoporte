package com.aym.modelo;

import com.aym.modelo.enums.TipoRegistro;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record RegistroSeguimiento(
        LocalDateTime fecha,
        TipoRegistro tipo,
        String detalle,
        Usuario autor
) {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public RegistroSeguimiento {
        if (fecha == null) fecha = LocalDateTime.now();
        if (tipo == null) throw new IllegalArgumentException("Tipo de registro obligatorio");
        if (detalle == null || detalle.isBlank()) {
            throw new IllegalArgumentException("Detalle del registro obligatorio");
        }
        if (autor == null) throw new IllegalArgumentException("Autor del registro obligatorio");
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (por %s)",
                fecha.format(FMT), tipo, detalle, autor.getUsername());
    }
}
