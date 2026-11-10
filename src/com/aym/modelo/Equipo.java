package com.aym.modelo;

public record Equipo(String tipo, String marca, String modelo) {
    public Equipo {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de equipo es obligatorio");
        }
        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("La marca del equipo es obligatoria");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("El modelo del equipo es obligatorio");
        }
    }

    @Override
    public String toString() {
        return tipo + " " + marca + " " + modelo;
    }
}
