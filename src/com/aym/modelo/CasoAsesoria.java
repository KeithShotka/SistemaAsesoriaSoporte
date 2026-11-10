package com.aym.modelo;

import com.aym.modelo.enums.*;
import com.aym.excepcion.TransicionInvalidaException;

public final class CasoAsesoria extends CasoSoporte {

    private String productoOServicioConsultado;
    private String recomendacion;

    public CasoAsesoria(Cliente cliente, String asunto, String descripcion,
                        AreaTecnica areaTecnica, String productoOServicioConsultado) {
        super(cliente, asunto, descripcion, areaTecnica, Prioridad.BAJA);
        this.productoOServicioConsultado = productoOServicioConsultado;
    }

    public String getProductoOServicioConsultado() {
        return productoOServicioConsultado;
    }

    public String getRecomendacion() {
        return recomendacion;
    }

    public void registrarRecomendacion(String texto, Asesor asesor) {
        if (getEstado() != EstadoCaso.EN_ATENCION) {
            throw new TransicionInvalidaException("Solo en EN_ATENCION se registra recomendación.");
        }
        if (getAsesor() == null || !getAsesor().getId().equals(asesor.getId())) {
            throw new TransicionInvalidaException("Solo el asesor asignado puede registrar.");
        }
        if (texto == null || texto.isBlank()) {
            throw new TransicionInvalidaException("La recomendación no puede estar vacía.");
        }
        this.recomendacion = texto;
        agregarRegistro(TipoRegistro.RECOMENDACION, texto, asesor);
        getCliente().notificar("Nueva recomendación en su caso " + getId());
    }

    @Override
    public TipoAtencion getTipoAtencion() {
        return TipoAtencion.ASESORIA;
    }

    @Override
    public boolean puedeResolverse() {
        return recomendacion != null && !recomendacion.isBlank() && getServicio() != null;
    }

    @Override
    public String getDetalleAtencion() {
        return "Asesoría sobre: " + productoOServicioConsultado
                + (recomendacion != null ? " | Recomendación: " + recomendacion : "");
    }
}
