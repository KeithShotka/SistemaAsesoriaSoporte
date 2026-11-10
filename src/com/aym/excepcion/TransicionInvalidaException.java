package com.aym.excepcion;

public class TransicionInvalidaException extends ReglaNegocioException {
    public TransicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
