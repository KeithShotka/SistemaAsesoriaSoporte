package com.aym.excepcion;

public class CasoNoEncontradoException extends RuntimeException {
    public CasoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
