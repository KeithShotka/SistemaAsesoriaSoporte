package com.aym.repositorio;

import com.aym.modelo.Identificable;
import java.util.*;

public class RepositorioMemoria<T extends Identificable> implements IRepositorio<T> {

    private final Map<String, T> datos = new HashMap<>();

    @Override
    public void guardar(T entidad) {
        datos.put(entidad.getId(), entidad);
    }

    @Override
    public Optional<T> buscarPorId(String id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public List<T> listar() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public boolean eliminar(String id) {
        return datos.remove(id) != null;
    }

    @Override
    public boolean contiene(String id) {
        return datos.containsKey(id);
    }
}
