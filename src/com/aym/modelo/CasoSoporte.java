package com.aym.modelo;

import com.aym.modelo.enums.*;
import com.aym.excepcion.PagoPendienteException;
import com.aym.excepcion.TransicionInvalidaException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract sealed class CasoSoporte implements IPagable, Identificable
        permits CasoAsesoria, CasoSoporteTecnico {

    private final String id;
    private final LocalDateTime fechaRegistro;
    private String asunto;
    private String descripcion;
    private AreaTecnica areaTecnica;
    private Prioridad prioridad;
    private EstadoCaso estado;
    private final Cliente cliente;
    private Asesor asesor;
    private Servicio servicio;
    private double ajusteCosto;
    private Pago pago;
    private final List<RegistroSeguimiento> historial;
    private final List<Cita> citas;

    protected CasoSoporte(Cliente cliente, String asunto, String descripcion,
                          AreaTecnica areaTecnica, Prioridad prioridadInicial) {
        if (cliente == null) throw new IllegalArgumentException("Cliente obligatorio");
        if (asunto == null || asunto.isBlank()) throw new IllegalArgumentException("Asunto obligatorio");
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("Descripción obligatoria");
        }
        this.id = generarCodigo();
        this.fechaRegistro = LocalDateTime.now();
        this.cliente = cliente;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.areaTecnica = areaTecnica;
        this.prioridad = prioridadInicial;
        this.estado = EstadoCaso.REGISTRADO;
        this.historial = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.ajusteCosto = 0.0;

        cliente.agregarCaso(this);
        agregarRegistro(TipoRegistro.NOTA, "Caso registrado: " + asunto, cliente);
    }

    private String generarCodigo() {
        return "CAS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String getId() {
        return id;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public AreaTecnica getAreaTecnica() {
        return areaTecnica;
    }

    public void setAreaTecnica(AreaTecnica areaTecnica) {
        this.areaTecnica = areaTecnica;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public EstadoCaso getEstado() {
        return estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Asesor getAsesor() {
        return asesor;
    }

    public void setAsesor(Asesor asesor) {
        this.asesor = asesor;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(citas);
    }

    public void agregarCita(Cita cita) {
        citas.add(cita);
    }

    public List<RegistroSeguimiento> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    public String getHistorialFormateado() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Historial del caso ").append(id).append(" ===\n");
        for (RegistroSeguimiento r : historial) {
            sb.append(r).append("\n");
        }
        return sb.toString();
    }

    public void agregarRegistro(TipoRegistro tipo, String detalle, Usuario autor) {
        historial.add(new RegistroSeguimiento(LocalDateTime.now(), tipo, detalle, autor));
    }

    public void cambiarEstado(EstadoCaso nuevoEstado, Usuario autor) {
        this.estado = nuevoEstado;
        agregarRegistro(TipoRegistro.CAMBIO_ESTADO,
                "Estado cambiado a " + nuevoEstado, autor);
    }

    public void iniciarAtencion(Asesor asesor) {
        if (this.asesor == null || !this.asesor.getId().equals(asesor.getId())) {
            throw new TransicionInvalidaException("El caso no está asignado a este asesor.");
        }
        if (estado != EstadoCaso.ASIGNADO) {
            throw new TransicionInvalidaException(
                    "Solo se puede iniciar atención desde ASIGNADO. Actual: " + estado);
        }
        cambiarEstado(EstadoCaso.EN_ATENCION, asesor);
        agregarRegistro(TipoRegistro.NOTA, "Atención iniciada", asesor);
        cliente.notificar("Su caso " + id + " está en atención.");
    }

    public void definirServicioYCosto(Servicio servicio, double ajuste, Asesor asesor) {
        if (estado != EstadoCaso.EN_ATENCION) {
            throw new TransicionInvalidaException("Solo en EN_ATENCION se puede definir servicio.");
        }
        if (this.asesor == null || !this.asesor.getId().equals(asesor.getId())) {
            throw new TransicionInvalidaException("Solo el asesor asignado puede definir el servicio.");
        }
        if (servicio.getTipoAtencion() != getTipoAtencion()) {
            throw new TransicionInvalidaException(
                    "El servicio no corresponde al tipo de atención del caso.");
        }
        double costo = servicio.getPrecioBase() + ajuste;
        if (costo < 0) {
            throw new TransicionInvalidaException("El costo resultante no puede ser negativo.");
        }
        this.servicio = servicio;
        this.ajusteCosto = ajuste;
        agregarRegistro(TipoRegistro.NOTA,
                "Servicio definido: " + servicio.getNombre()
                        + " | Costo: S/ " + String.format("%.2f", costo), asesor);
    }

    @Override
    public double getCosto() {
        if (servicio == null) return 0.0;
        return servicio.getPrecioBase() + ajusteCosto;
    }

    @Override
    public boolean estaPagado() {
        return pago != null && pago.getEstado() == EstadoPago.PAGADO;
    }

    @Override
    public void registrarPago(Pago pago) {
        this.pago = pago;
    }

    public Pago getPago() {
        return pago;
    }

    public void resolver(Asesor asesor) {
        if (estado != EstadoCaso.EN_ATENCION) {
            throw new TransicionInvalidaException(
                    "Solo se puede resolver un caso EN_ATENCION. Actual: " + estado);
        }
        if (this.asesor == null || !this.asesor.getId().equals(asesor.getId())) {
            throw new TransicionInvalidaException("Solo el asesor asignado puede resolver el caso.");
        }
        if (servicio == null) {
            throw new TransicionInvalidaException("Debe definir servicio y costo antes de resolver.");
        }
        if (!puedeResolverse()) {
            throw new TransicionInvalidaException(
                    "No se cumplen los requisitos para resolver este tipo de caso.");
        }
        cambiarEstado(EstadoCaso.RESUELTO, asesor);
        cliente.notificar("Su caso " + id + " ha sido resuelto.");
    }

    public void cerrar(Asesor asesor) {
        if (estado != EstadoCaso.PAGADO) {
            throw new PagoPendienteException(
                    "No se puede cerrar el caso sin pago. Estado actual: " + estado);
        }
        if (this.asesor == null || !this.asesor.getId().equals(asesor.getId())) {
            throw new TransicionInvalidaException("Solo el asesor asignado puede cerrar el caso.");
        }
        cambiarEstado(EstadoCaso.CERRADO, asesor);
        cliente.notificar("Su caso " + id + " ha sido cerrado.");
    }

    public abstract TipoAtencion getTipoAtencion();

    public abstract boolean puedeResolverse();

    public abstract String getDetalleAtencion();

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | Prioridad: %s | Cliente: %s",
                id, getTipoAtencion(), estado, areaTecnica, prioridad, cliente.getNombreCompleto());
    }
}
