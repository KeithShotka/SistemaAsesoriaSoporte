package com.aym.modelo;

public final class Factura extends ComprobantePago {

    public Factura(String serie, String numero, double total, Cliente cliente) {
        super(serie, numero, total, cliente);
    }

    @Override
    public String getTipo() {
        return "FACTURA";
    }

    @Override
    public String getDatosCliente() {
        String razon = getCliente().getRazonSocial() != null
                ? getCliente().getRazonSocial()
                : getCliente().getNombreCompleto();
        return "RUC " + getCliente().getNumeroDocumento() + " - " + razon;
    }
}
