package com.aym.modelo;

public final class Boleta extends ComprobantePago {

    public Boleta(String serie, String numero, double total, Cliente cliente) {
        super(serie, numero, total, cliente);
    }

    @Override
    public String getTipo() {
        return "BOLETA";
    }

    @Override
    public String getDatosCliente() {
        return "DNI " + getCliente().getNumeroDocumento() + " - " + getCliente().getNombreCompleto();
    }
}
