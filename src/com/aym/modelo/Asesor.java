package com.aym.modelo;

import com.aym.modelo.enums.AreaTecnica;
import com.aym.modelo.enums.Rol;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public final class Asesor extends Usuario {

    private final Set<AreaTecnica> especialidades;
    private final List<CasoSoporte> casosAsignados;

    public Asesor(String username, String password, String nombreCompleto,
                  Set<AreaTecnica> especialidades) {
        super(username, password, nombreCompleto, Rol.ASESOR);
        this.especialidades = especialidades != null
                ? EnumSet.copyOf(especialidades)
                : EnumSet.noneOf(AreaTecnica.class);
        this.casosAsignados = new ArrayList<>();
    }

    public Set<AreaTecnica> getEspecialidades() {
        return especialidades;
    }

    public boolean domina(AreaTecnica area) {
        return especialidades.contains(area);
    }

    public List<CasoSoporte> getCasosAsignados() {
        return casosAsignados;
    }

    public int cargaActiva() {
        return (int) casosAsignados.stream()
                .filter(c -> c.getEstado() != com.aym.modelo.enums.EstadoCaso.CERRADO
                        && c.getEstado() != com.aym.modelo.enums.EstadoCaso.RESUELTO
                        && c.getEstado() != com.aym.modelo.enums.EstadoCaso.PAGADO)
                .count();
    }

    public void asignarCaso(CasoSoporte caso) {
        casosAsignados.add(caso);
    }

    @Override
    public String getPerfil() {
        return "Asesor Técnico: " + getNombreCompleto()
                + " | Especialidades: " + especialidades
                + " | Carga activa: " + cargaActiva();
    }

    @Override
    public String getCanal() {
        return "App del asesor / Interno";
    }
}
