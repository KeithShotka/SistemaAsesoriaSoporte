package com.aym.modelo;

import com.aym.modelo.enums.MetodoPago;

public interface ICobrador {
    Pago cobrar(CasoSoporte caso, MetodoPago metodo);
    ComprobantePago emitirComprobante(Pago pago, Cliente cliente);
}
