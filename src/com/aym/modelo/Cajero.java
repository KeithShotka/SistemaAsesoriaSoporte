package com.aym.modelo;

import com.aym.modelo.enums.*;
import com.aym.excepcion.ReglaNegocioException;
import com.aym.excepcion.TransicionInvalidaException;

public final class Cajero extends Usuario implements ICobrador {

    private int correlativoBoleta = 1;
    private int correlativoFactura = 1;

    public Cajero(String username, String password, String nombreCompleto) {
        super(username, password, nombreCompleto, Rol.CAJERO);
    }

    @Override
    public Pago cobrar(CasoSoporte caso, MetodoPago metodo) {
        if (caso.getEstado() != EstadoCaso.RESUELTO) {
            throw new TransicionInvalidaException(
                    "Solo se puede cobrar un caso RESUELTO. Actual: " + caso.getEstado());
        }
        if (caso.estaPagado()) {
            throw new ReglaNegocioException("El caso ya está pagado.");
        }
        if (caso.getCosto() <= 0) {
            throw new ReglaNegocioException("El caso no tiene costo definido.");
        }
        if (metodo == null) {
            throw new ReglaNegocioException("Debe indicar el método de pago.");
        }

        Pago pago = new Pago(caso.getCosto(), metodo);
        caso.registrarPago(pago);
        caso.cambiarEstado(EstadoCaso.PAGADO, this);
        caso.agregarRegistro(TipoRegistro.PAGO,
                "Pago registrado: S/ " + String.format("%.2f", pago.getMonto()) + " por " + metodo, this);

        ComprobantePago comprobante = emitirComprobante(pago, caso.getCliente());
        pago.setComprobante(comprobante);

        caso.getCliente().notificar("Pago registrado para el caso " + caso.getId()
                + ". Comprobante: " + comprobante.getNumeroCompleto());
        return pago;
    }

    @Override
    public ComprobantePago emitirComprobante(Pago pago, Cliente cliente) {
        if (cliente.getTipoDocumento() == null || cliente.getNumeroDocumento() == null) {
            throw new ReglaNegocioException("El cliente no tiene documento registrado.");
        }

        ComprobantePago comprobante;
        if (cliente.getTipoDocumento() == TipoDocumento.DNI) {
            String serie = "B001";
            String numero = String.format("%08d", correlativoBoleta++);
            comprobante = new Boleta(serie, numero, pago.getMonto(), cliente);
        } else {
            String serie = "F001";
            String numero = String.format("%08d", correlativoFactura++);
            comprobante = new Factura(serie, numero, pago.getMonto(), cliente);
        }
        return comprobante;
    }

    @Override
    public String getPerfil() {
        return "Cajero: " + getNombreCompleto();
    }

    @Override
    public String getCanal() {
        return "App de caja / Interno";
    }
}
