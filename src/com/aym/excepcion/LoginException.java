package com.aym.excepcion;

public class LoginException extends RuntimeException {
    public LoginException(String mensaje) {
        super(mensaje);
    }
}
