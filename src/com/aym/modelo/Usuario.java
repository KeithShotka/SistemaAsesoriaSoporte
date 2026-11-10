package com.aym.modelo;

import com.aym.modelo.enums.Rol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public abstract sealed class Usuario implements INotificable, Identificable
        permits Cliente, Asesor, Coordinador, Cajero {

    private final String id;
    private String username;
    private String password;
    private String nombreCompleto;
    private final Rol rol;
    private final List<String> notificaciones;

    protected Usuario(String username, String password, String nombreCompleto, Rol rol) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username obligatorio");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password obligatorio");
        }
        this.id = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.notificaciones = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean autenticar(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Rol getRol() {
        return rol;
    }

    @Override
    public void notificar(String mensaje) {
        notificaciones.add(mensaje);
    }

    @Override
    public List<String> getNotificaciones() {
        return Collections.unmodifiableList(notificaciones);
    }

    public abstract String getPerfil();

    @Override
    public abstract String getCanal();

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", nombreCompleto, username, rol);
    }
}
