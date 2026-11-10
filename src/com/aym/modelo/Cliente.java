package com.aym.modelo;

import com.aym.modelo.enums.Rol;
import com.aym.modelo.enums.TipoDocumento;
import java.util.ArrayList;
import java.util.List;

public final class Cliente extends Usuario {

    private TipoDocumento tipoDocumento;
    private String numeroDocumento;
    private String razonSocial; // solo para RUC
    private final List<CasoSoporte> casos;

    public Cliente(String username, String password, String nombreCompleto,
                   TipoDocumento tipoDocumento, String numeroDocumento) {
        super(username, password, nombreCompleto, Rol.CLIENTE);
        if (tipoDocumento == null || numeroDocumento == null || numeroDocumento.isBlank()) {
            throw new IllegalArgumentException("Documento del cliente es obligatorio");
        }
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.casos = new ArrayList<>();
    }

    public Cliente(String username, String password, String nombreCompleto,
                   TipoDocumento tipoDocumento, String numeroDocumento, String razonSocial) {
        this(username, password, nombreCompleto, tipoDocumento, numeroDocumento);
        this.razonSocial = razonSocial;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public List<CasoSoporte> getCasos() {
        return casos;
    }

    public void agregarCaso(CasoSoporte caso) {
        casos.add(caso);
    }

    public String consultarSeguimiento(String codigoCaso) {
        return casos.stream()
                .filter(c -> c.getId().equals(codigoCaso))
                .findFirst()
                .map(CasoSoporte::getHistorialFormateado)
                .orElse("Caso no encontrado");
    }

    @Override
    public String getPerfil() {
        return "Cliente: " + getNombreCompleto() + " | Doc: " + tipoDocumento + " " + numeroDocumento;
    }

    @Override
    public String getCanal() {
        return "App / Email del cliente";
    }
}
