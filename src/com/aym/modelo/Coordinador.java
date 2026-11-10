package com.aym.modelo;

import com.aym.modelo.enums.*;
import com.aym.excepcion.TransicionInvalidaException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Coordinador extends Usuario {

    private final List<CasoSoporte> casosGestionados;

    public Coordinador(String username, String password, String nombreCompleto) {
        super(username, password, nombreCompleto, Rol.COORDINADOR);
        this.casosGestionados = new ArrayList<>();
    }

    public List<CasoSoporte> getCasosGestionados() {
        return casosGestionados;
    }

    public void clasificarCaso(CasoSoporte caso, AreaTecnica area, Prioridad prioridad) {
        if (caso.getEstado() != EstadoCaso.REGISTRADO) {
            throw new TransicionInvalidaException(
                    "Solo se puede clasificar un caso en estado REGISTRADO. Actual: " + caso.getEstado());
        }
        caso.setAreaTecnica(area);
        caso.setPrioridad(prioridad);
        caso.cambiarEstado(EstadoCaso.CLASIFICADO, this);
        casosGestionados.add(caso);
        caso.agregarRegistro(TipoRegistro.CLASIFICACION,
                "Clasificado: área=" + area + ", prioridad=" + prioridad, this);
        caso.getCliente().notificar("Su caso " + caso.getId() + " ha sido clasificado.");
    }

    public void asignarCaso(CasoSoporte caso, Asesor asesor) {
        if (caso.getEstado() != EstadoCaso.CLASIFICADO) {
            throw new TransicionInvalidaException(
                    "Solo se puede asignar un caso en estado CLASIFICADO. Actual: " + caso.getEstado());
        }
        caso.setAsesor(asesor);
        asesor.asignarCaso(caso);
        caso.cambiarEstado(EstadoCaso.ASIGNADO, this);
        caso.agregarRegistro(TipoRegistro.ASIGNACION,
                "Asignado al asesor " + asesor.getNombreCompleto(), this);
        asesor.notificar("Se le ha asignado el caso " + caso.getId());
        caso.getCliente().notificar("Su caso " + caso.getId() + " fue asignado a un asesor.");
    }

    public Cita programarCita(CasoSoporte caso, LocalDateTime fechaHora,
                              ModalidadCita modalidad, String motivo) {
        if (caso.getEstado() != EstadoCaso.ASIGNADO
                && caso.getEstado() != EstadoCaso.EN_ATENCION) {
            throw new TransicionInvalidaException(
                    "Solo se puede programar cita en ASIGNADO o EN_ATENCION. Actual: " + caso.getEstado());
        }
        Cita cita = new Cita(fechaHora, modalidad, motivo);
        caso.agregarCita(cita);
        caso.agregarRegistro(TipoRegistro.CITA,
                "Cita programada: " + cita, this);
        caso.getCliente().notificar("Cita programada para su caso " + caso.getId() + ": " + cita);
        if (caso.getAsesor() != null) {
            caso.getAsesor().notificar("Nueva cita en caso " + caso.getId() + ": " + cita);
        }
        return cita;
    }

    public Map<EstadoCaso, Long> resumenPorEstado(List<CasoSoporte> todosLosCasos) {
        return todosLosCasos.stream()
                .collect(Collectors.groupingBy(CasoSoporte::getEstado, Collectors.counting()));
    }

    @Override
    public String getPerfil() {
        return "Coordinador: " + getNombreCompleto()
                + " | Casos gestionados: " + casosGestionados.size();
    }

    @Override
    public String getCanal() {
        return "App del coordinador / Interno";
    }
}
