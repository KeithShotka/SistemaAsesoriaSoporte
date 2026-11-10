package com.aym.modelo;

import com.aym.modelo.enums.EstadoPago;
import com.aym.modelo.enums.MetodoPago;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pago implements Identificable {

    private final String id;
    private final double monto;
    private final LocalDateTime fecha;
    private final MetodoPago metodo;
    private EstadoPago estado;
    private ComprobantePago comprobante;

    public Pago(double monto, MetodoPago metodo) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (metodo == null) {
            throw new IllegalArgumentException("El método de pago es obligatorio");
        }
        this.id = "PAG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.metodo = metodo;
        this.estado = EstadoPago.PAGADO;
    }

    @Override
    public String getId() {
        return id;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public MetodoPago getMetodo() {
        return metodo;
    }

    public EstadoPago getEstado() {
        return estado;
    }

    public void setEstado(EstadoPago estado) {
        this.estado = estado;
    }

    public ComprobantePago getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobantePago comprobante) {
        this.comprobante = comprobante;
    }

    @Override
    public String toString() {
        return String.format("Pago %s | S/ %.2f | %s | %s", id, monto, metodo, estado);
    }
}
