package com.aym.repositorio;

import com.aym.modelo.Identificable;
import java.util.List;
import java.util.Optional;

public interface IRepositorio<T extends Identificable> {
    void guardar(T entidad);
    Optional<T> buscarPorId(String id);
    List<T> listar();
    boolean eliminar(String id);
    boolean contiene(String id);
}
