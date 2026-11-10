package com.aym.servicio;

import com.aym.modelo.Servicio;
import com.aym.modelo.enums.AreaTecnica;
import com.aym.modelo.enums.TipoAtencion;
import com.aym.repositorio.IRepositorio;
import com.aym.repositorio.RepositorioMemoria;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CatalogoServicios {

    private final IRepositorio<Servicio> repo;

    public CatalogoServicios() {
        this.repo = new RepositorioMemoria<>();
        cargarServiciosIniciales();
    }

    private void cargarServiciosIniciales() {
        agregar("Asesoría de hardware", "Orientación sobre componentes y equipos",
                AreaTecnica.HARDWARE, TipoAtencion.ASESORIA, 50.0);
        agregar("Asesoría de software", "Orientación sobre programas y licencias",
                AreaTecnica.SOFTWARE, TipoAtencion.ASESORIA, 40.0);
        agregar("Asesoría de redes", "Orientación sobre configuración de redes",
                AreaTecnica.REDES, TipoAtencion.ASESORIA, 60.0);
        agregar("Diagnóstico y reparación de PC", "Soporte de hardware de computadoras",
                AreaTecnica.HARDWARE, TipoAtencion.SOPORTE_TECNICO, 120.0);
        agregar("Instalación y configuración de software", "Soporte de software",
                AreaTecnica.SOFTWARE, TipoAtencion.SOPORTE_TECNICO, 80.0);
        agregar("Configuración de red y conectividad", "Soporte de redes",
                AreaTecnica.REDES, TipoAtencion.SOPORTE_TECNICO, 100.0);
        agregar("Mantenimiento preventivo", "Limpieza y optimización de equipos",
                AreaTecnica.HARDWARE, TipoAtencion.SOPORTE_TECNICO, 90.0);
    }

    public Servicio agregar(String nombre, String descripcion, AreaTecnica area,
                            TipoAtencion tipo, double precio) {
        Servicio s = new Servicio(nombre, descripcion, area, tipo, precio);
        repo.guardar(s);
        return s;
    }

    public void modificarPrecio(String id, double nuevoPrecio) {
        Servicio s = repo.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Servicio no encontrado"));
        s.setPrecioBase(nuevoPrecio);
    }

    public boolean eliminar(String id) {
        return repo.eliminar(id);
    }

    public Optional<Servicio> buscarPorId(String id) {
        return repo.buscarPorId(id);
    }

    public List<Servicio> listar() {
        return repo.listar();
    }

    public List<Servicio> listarPorTipo(TipoAtencion tipo) {
        return repo.listar().stream()
                .filter(s -> s.getTipoAtencion() == tipo)
                .collect(Collectors.toList());
    }

    public List<Servicio> listarPorArea(AreaTecnica area) {
        return repo.listar().stream()
                .filter(s -> s.getArea() == area)
                .collect(Collectors.toList());
    }
}
