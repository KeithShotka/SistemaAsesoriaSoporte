package com.aym.modelo;

import com.aym.modelo.enums.EstadoCita;
import com.aym.modelo.enums.ModalidadCita;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Cita implements Identificable {

    private final String id;
    private LocalDateTime fechaHora;
    private ModalidadCita modalidad;
    private EstadoCita estado;
    private String motivo;

    public Cita(LocalDateTime fechaHora, ModalidadCita modalidad, String motivo) {
        if (fechaHora == null || fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la cita debe ser futura");
        }
        if (modalidad == null) {
            throw new IllegalArgumentException("La modalidad es obligatoria");
        }
        this.id = "CIT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.fechaHora = fechaHora;
        this.modalidad = modalidad;
        this.estado = EstadoCita.PROGRAMADA;
        this.motivo = motivo != null ? motivo : "";
    }

    @Override
    public String getId() {
        return id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public ModalidadCita getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadCita modalidad) {
        this.modalidad = modalidad;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Cita %s | %s | %s | %s | %s",
                id, fechaHora.format(fmt), modalidad, estado, motivo);
    }
}
