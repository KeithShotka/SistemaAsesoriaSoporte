package com.aym.modelo;

import java.util.UUID;

public abstract sealed class ComprobantePago implements Identificable
        permits Boleta, Factura {

    private final String id;
    private final String serie;
    private final String numero;
    private final double total;
    private final double subtotal;
    private final double igv;
    private final Cliente cliente;

    protected ComprobantePago(String serie, String numero, double total, Cliente cliente) {
        this.id = "CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.serie = serie;
        this.numero = numero;
        this.total = total;
        this.subtotal = total / 1.18;
        this.igv = total - this.subtotal;
        this.cliente = cliente;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getSerie() {
        return serie;
    }

    public String getNumero() {
        return numero;
    }

    public String getNumeroCompleto() {
        return serie + "-" + numero;
    }

    public double getTotal() {
        return total;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getIgv() {
        return igv;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public abstract String getTipo();

    public abstract String getDatosCliente();

    @Override
    public String toString() {
        return String.format("%s %s | Total: S/ %.2f | Cliente: %s",
                getTipo(), getNumeroCompleto(), total, getDatosCliente());
    }
}
