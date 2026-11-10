package com.aym.servicio;

import com.aym.modelo.Pago;
import com.aym.repositorio.IRepositorio;
import com.aym.repositorio.RepositorioMemoria;

import java.util.List;
import java.util.Optional;

public class GestorCobranza {

    private final IRepositorio<Pago> repo;

    public GestorCobranza() {
        this.repo = new RepositorioMemoria<>();
    }

    public void registrar(Pago pago) {
        repo.guardar(pago);
    }

    public Optional<Pago> buscarPorId(String id) {
        return repo.buscarPorId(id);
    }

    public List<Pago> listar() {
        return repo.listar();
    }
}
