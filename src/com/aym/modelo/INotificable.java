package com.aym.modelo;

import java.util.List;

public interface INotificable {
    void notificar(String mensaje);
    List<String> getNotificaciones();
    String getCanal();
}
