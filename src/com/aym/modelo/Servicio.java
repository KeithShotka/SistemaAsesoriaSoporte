package com.aym.modelo;

import com.aym.modelo.enums.AreaTecnica;
import com.aym.modelo.enums.TipoAtencion;
import java.util.UUID;

public class Servicio implements Identificable {

    private final String id;
    private String nombre;
    private String descripcion;
    private AreaTecnica area;
    private TipoAtencion tipoAtencion;
    private double precioBase;

    public Servicio(String nombre, String descripcion, AreaTecnica area,
                    TipoAtencion tipoAtencion, double precioBase) {
        this.id = "SRV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.area = area;
        this.tipoAtencion = tipoAtencion;
        this.precioBase = precioBase;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public AreaTecnica getArea() {
        return area;
    }

    public void setArea(AreaTecnica area) {
        this.area = area;
    }

    public TipoAtencion getTipoAtencion() {
        return tipoAtencion;
    }

    public void setTipoAtencion(TipoAtencion tipoAtencion) {
        this.tipoAtencion = tipoAtencion;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio base no puede ser negativo");
        }
        this.precioBase = precioBase;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | S/ %.2f", nombre, area, tipoAtencion, precioBase);
    }
}
