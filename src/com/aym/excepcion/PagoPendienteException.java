package com.aym.excepcion;

public class PagoPendienteException extends ReglaNegocioException {
    public PagoPendienteException(String mensaje) {
        super(mensaje);
    }
}
