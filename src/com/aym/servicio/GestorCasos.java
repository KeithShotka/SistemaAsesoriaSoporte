package com.aym.servicio;

import com.aym.excepcion.CasoNoEncontradoException;
import com.aym.modelo.*;
import com.aym.modelo.enums.*;
import com.aym.repositorio.IRepositorio;
import com.aym.repositorio.RepositorioMemoria;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GestorCasos {

    private final IRepositorio<CasoSoporte> repo;

    public GestorCasos() {
        this.repo = new RepositorioMemoria<>();
    }

    public GestorCasos(IRepositorio<CasoSoporte> repo) {
        this.repo = repo;
    }

    public CasoAsesoria registrarAsesoria(Cliente cliente, String asunto, String descripcion,
                                          AreaTecnica area, String productoConsultado) {
        CasoAsesoria caso = new CasoAsesoria(cliente, asunto, descripcion, area, productoConsultado);
        repo.guardar(caso);
        cliente.notificar("Su solicitud de asesoría fue registrada. Código: " + caso.getId());
        return caso;
    }

    public CasoSoporteTecnico registrarSoporte(Cliente cliente, String asunto, String descripcion,
                                               AreaTecnica area, Equipo equipo) {
        CasoSoporteTecnico caso = new CasoSoporteTecnico(cliente, asunto, descripcion, area, equipo);
        repo.guardar(caso);
        cliente.notificar("Su solicitud de soporte técnico fue registrada. Código: " + caso.getId());
        return caso;
    }

    public Optional<CasoSoporte> buscarPorId(String id) {
        return repo.buscarPorId(id);
    }

    public CasoSoporte obtener(String id) {
        return repo.buscarPorId(id)
                .orElseThrow(() -> new CasoNoEncontradoException("Caso no encontrado: " + id));
    }

    public List<CasoSoporte> listarTodos() {
        return repo.listar();
    }

    public List<CasoSoporte> listarPorEstado(EstadoCaso estado) {
        return repo.listar().stream()
                .filter(c -> c.getEstado() == estado)
                .collect(Collectors.toList());
    }

    public List<CasoSoporte> listarPorCliente(Cliente cliente) {
        return repo.listar().stream()
                .filter(c -> c.getCliente().getId().equals(cliente.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Sugiere el asesor que domina el área del caso y tiene menor carga activa.
     * Si nadie domina el área, considera a todos.
     */
    public Optional<Asesor> sugerirAsesor(CasoSoporte caso, List<Asesor> asesores) {
        if (asesores == null || asesores.isEmpty()) {
            return Optional.empty();
        }

        List<Asesor> candidatos = asesores.stream()
                .filter(a -> a.domina(caso.getAreaTecnica()))
                .collect(Collectors.toList());

        if (candidatos.isEmpty()) {
            candidatos = asesores;
        }

        return candidatos.stream()
                .min(Comparator.comparingInt(Asesor::cargaActiva));
    }
}
