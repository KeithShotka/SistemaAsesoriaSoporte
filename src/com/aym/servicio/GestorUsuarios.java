package com.aym.servicio;

import com.aym.excepcion.LoginException;
import com.aym.modelo.*;
import com.aym.modelo.enums.AreaTecnica;
import com.aym.modelo.enums.TipoDocumento;
import com.aym.repositorio.IRepositorio;
import com.aym.repositorio.RepositorioMemoria;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GestorUsuarios {

    private final IRepositorio<Usuario> repo;

    public GestorUsuarios() {
        this.repo = new RepositorioMemoria<>();
    }

    public GestorUsuarios(IRepositorio<Usuario> repo) {
        this.repo = repo;
    }

    public Cliente registrarCliente(String username, String password, String nombre,
                                    TipoDocumento tipoDoc, String numDoc) {
        validarUsernameLibre(username);
        Cliente c = new Cliente(username, password, nombre, tipoDoc, numDoc);
        repo.guardar(c);
        return c;
    }

    public Cliente registrarClienteEmpresa(String username, String password, String nombre,
                                           String ruc, String razonSocial) {
        validarUsernameLibre(username);
        Cliente c = new Cliente(username, password, nombre, TipoDocumento.RUC, ruc, razonSocial);
        repo.guardar(c);
        return c;
    }

    public Asesor registrarAsesor(String username, String password, String nombre,
                                  Set<AreaTecnica> especialidades) {
        validarUsernameLibre(username);
        Asesor a = new Asesor(username, password, nombre, especialidades);
        repo.guardar(a);
        return a;
    }

    public Coordinador registrarCoordinador(String username, String password, String nombre) {
        validarUsernameLibre(username);
        Coordinador c = new Coordinador(username, password, nombre);
        repo.guardar(c);
        return c;
    }

    public Cajero registrarCajero(String username, String password, String nombre) {
        validarUsernameLibre(username);
        Cajero c = new Cajero(username, password, nombre);
        repo.guardar(c);
        return c;
    }

    private void validarUsernameLibre(String username) {
        if (buscarPorUsername(username).isPresent()) {
            throw new IllegalArgumentException("El username ya está registrado: " + username);
        }
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return repo.listar().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public Usuario autenticar(String username, String password) {
        Usuario u = buscarPorUsername(username)
                .orElseThrow(() -> new LoginException("Usuario no encontrado: " + username));
        if (!u.autenticar(password)) {
            throw new LoginException("Contraseña incorrecta para: " + username);
        }
        return u;
    }

    public List<Asesor> listarAsesores() {
        return repo.listar().stream()
                .filter(u -> u instanceof Asesor)
                .map(u -> (Asesor) u)
                .collect(Collectors.toList());
    }

    public List<Usuario> listarTodos() {
        return repo.listar();
    }
}
