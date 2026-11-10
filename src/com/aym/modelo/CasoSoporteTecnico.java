package com.aym.modelo;

import com.aym.modelo.enums.*;
import com.aym.excepcion.TransicionInvalidaException;

public final class CasoSoporteTecnico extends CasoSoporte {

    private final Equipo equipo;
    private String diagnostico;
    private String solucionAplicada;

    public CasoSoporteTecnico(Cliente cliente, String asunto, String descripcion,
                              AreaTecnica areaTecnica, Equipo equipo) {
        super(cliente, asunto, descripcion, areaTecnica, Prioridad.MEDIA);
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo es obligatorio en soporte técnico.");
        }
        this.equipo = equipo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getSolucionAplicada() {
        return solucionAplicada;
    }

    public void registrarDiagnostico(String texto, Asesor asesor) {
        if (getEstado() != EstadoCaso.EN_ATENCION) {
            throw new TransicionInvalidaException("Solo en EN_ATENCION se registra diagnóstico.");
        }
        if (getAsesor() == null || !getAsesor().getId().equals(asesor.getId())) {
            throw new TransicionInvalidaException("Solo el asesor asignado puede registrar.");
        }
        if (texto == null || texto.isBlank()) {
            throw new TransicionInvalidaException("El diagnóstico no puede estar vacío.");
        }
        this.diagnostico = texto;
        agregarRegistro(TipoRegistro.DIAGNOSTICO, texto, asesor);
        getCliente().notificar("Diagnóstico registrado en su caso " + getId());
    }

    public void registrarSolucion(String texto, Asesor asesor) {
        if (getEstado() != EstadoCaso.EN_ATENCION) {
            throw new TransicionInvalidaException("Solo en EN_ATENCION se registra solución.");
        }
        if (getAsesor() == null || !getAsesor().getId().equals(asesor.getId())) {
            throw new TransicionInvalidaException("Solo el asesor asignado puede registrar.");
        }
        if (diagnostico == null || diagnostico.isBlank()) {
            throw new TransicionInvalidaException(
                    "No se puede registrar solución sin diagnóstico previo.");
        }
        if (texto == null || texto.isBlank()) {
            throw new TransicionInvalidaException("La solución no puede estar vacía.");
        }
        this.solucionAplicada = texto;
        agregarRegistro(TipoRegistro.SOLUCION, texto, asesor);
        getCliente().notificar("Solución registrada en su caso " + getId());
    }

    @Override
    public TipoAtencion getTipoAtencion() {
        return TipoAtencion.SOPORTE_TECNICO;
    }

    @Override
    public boolean puedeResolverse() {
        return diagnostico != null && !diagnostico.isBlank()
                && solucionAplicada != null && !solucionAplicada.isBlank()
                && getServicio() != null;
    }

    @Override
    public String getDetalleAtencion() {
        return "Equipo: " + equipo
                + (diagnostico != null ? " | Diagnóstico: " + diagnostico : "")
                + (solucionAplicada != null ? " | Solución: " + solucionAplicada : "");
    }
}
