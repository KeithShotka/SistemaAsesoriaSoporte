package com.aym.modelo;

public interface IPagable {
    double getCosto();
    boolean estaPagado();
    void registrarPago(Pago pago);
}
