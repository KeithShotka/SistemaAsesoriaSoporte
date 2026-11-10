package com.aym.app;

import com.aym.excepcion.*;
import com.aym.modelo.*;
import com.aym.modelo.enums.*;
import com.aym.servicio.*;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("  SISTEMA DE GESTIÓN DE ASESORÍA Y SOPORTE TÉCNICO");
        System.out.println("  A&M Computer Solutions SAC");
        System.out.println("============================================================\n");

        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        GestorCasos gestorCasos = new GestorCasos();
        CatalogoServicios catalogo = new CatalogoServicios();
        GestorCobranza gestorCobranza = new GestorCobranza();

      
        System.out.println("--- 1. REGISTRO DE USUARIOS ---\n");

        Cliente cliente1 = gestorUsuarios.registrarCliente(
                "jperez", "clave123", "Juan Pérez",
                TipoDocumento.DNI, "45678912");
        System.out.println("Cliente registrado: " + cliente1.getPerfil());

        Cliente cliente2 = gestorUsuarios.registrarClienteEmpresa(
                "empresaX", "clave456", "María López",
                "20123456789", "Empresa X S.A.C.");
        System.out.println("Cliente empresa registrado: " + cliente2.getPerfil());

        Asesor asesorHW = gestorUsuarios.registrarAsesor(
                "asesor1", "tec123", "Carlos Ramírez",
                EnumSet.of(AreaTecnica.HARDWARE, AreaTecnica.REDES));
        System.out.println("Asesor registrado: " + asesorHW.getPerfil());

        Asesor asesorSW = gestorUsuarios.registrarAsesor(
                "asesor2", "tec456", "Ana Torres",
                EnumSet.of(AreaTecnica.SOFTWARE));
        System.out.println("Asesor registrado: " + asesorSW.getPerfil());

        Coordinador coord = gestorUsuarios.registrarCoordinador(
                "coord1", "admin123", "Luis Mendoza");
        System.out.println("Coordinador registrado: " + coord.getPerfil());

        Cajero cajero = gestorUsuarios.registrarCajero(
                "caja1", "caja123", "Pedro Sánchez");
        System.out.println("Cajero registrado: " + cajero.getPerfil());

     
        System.out.println("\n--- 2. AUTENTICACIÓN ---\n");

        try {
            Usuario u = gestorUsuarios.autenticar("jperez", "clave123");
            System.out.println("Login exitoso: " + u.getNombreCompleto() + " (" + u.getRol() + ")");
        } catch (LoginException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            gestorUsuarios.autenticar("jperez", "malaclave");
        } catch (LoginException e) {
            System.out.println("Login fallido (esperado): " + e.getMessage());
        }

     
        System.out.println("\n--- 3. REGISTRO DE SOLICITUDES ---\n");

        CasoAsesoria casoAsesoria = gestorCasos.registrarAsesoria(
                cliente1,
                "Consulta sobre laptop para oficina",
                "Necesito orientación para comprar una laptop adecuada para trabajo de oficina y videollamadas.",
                AreaTecnica.HARDWARE,
                "Laptop empresarial");
        System.out.println("Caso asesoría creado: " + casoAsesoria);

        Equipo equipo = new Equipo("Laptop", "HP", "Pavilion 15");
        CasoSoporteTecnico casoSoporte = gestorCasos.registrarSoporte(
                cliente2,
                "Laptop no enciende",
                "La laptop no enciende desde ayer. Se escuchaba un ruido antes de apagarse.",
                AreaTecnica.HARDWARE,
                equipo);
        System.out.println("Caso soporte creado: " + casoSoporte);
        System.out.println("  Equipo: " + casoSoporte.getEquipo());

       
        System.out.println("\n--- 4. CLASIFICACIÓN Y ASIGNACIÓN ---\n");

        coord.clasificarCaso(casoAsesoria, AreaTecnica.HARDWARE, Prioridad.BAJA);
        System.out.println("Caso asesoría clasificado: " + casoAsesoria.getEstado());

        coord.clasificarCaso(casoSoporte, AreaTecnica.HARDWARE, Prioridad.ALTA);
        System.out.println("Caso soporte clasificado: " + casoSoporte.getEstado()
                + " | Prioridad: " + casoSoporte.getPrioridad());

        List<Asesor> asesores = gestorUsuarios.listarAsesores();

        Asesor sugerido1 = gestorCasos.sugerirAsesor(casoAsesoria, asesores).orElse(null);
        System.out.println("Asesor sugerido para asesoría: "
                + (sugerido1 != null ? sugerido1.getNombreCompleto() : "ninguno"));
        if (sugerido1 != null) {
            coord.asignarCaso(casoAsesoria, sugerido1);
            System.out.println("Asignado. Estado: " + casoAsesoria.getEstado());
        }

        Asesor sugerido2 = gestorCasos.sugerirAsesor(casoSoporte, asesores).orElse(null);
        System.out.println("Asesor sugerido para soporte: "
                + (sugerido2 != null ? sugerido2.getNombreCompleto() : "ninguno"));
        if (sugerido2 != null) {
            coord.asignarCaso(casoSoporte, sugerido2);
            System.out.println("Asignado. Estado: " + casoSoporte.getEstado());
            System.out.println("Carga activa de " + sugerido2.getNombreCompleto()
                    + ": " + sugerido2.cargaActiva());
        }

  
        System.out.println("\n--- 5. PROGRAMAR CITA ---\n");

        Cita cita = coord.programarCita(
                casoSoporte,
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                ModalidadCita.PRESENCIAL,
                "Recepción del equipo para diagnóstico");
        System.out.println("Cita programada: " + cita);

 
        System.out.println("\n--- 6. ATENCIÓN DEL CASO DE SOPORTE ---\n");

        Asesor atiende = casoSoporte.getAsesor();
        casoSoporte.iniciarAtencion(atiende);
        System.out.println("Atención iniciada. Estado: " + casoSoporte.getEstado());

        System.out.println("\nIntentando registrar solución sin diagnóstico...");
        try {
            casoSoporte.registrarSolucion("Se cambió la fuente de poder", atiende);
        } catch (TransicionInvalidaException e) {
            System.out.println("  RECHAZADO (esperado): " + e.getMessage());
        }

        casoSoporte.registrarDiagnostico(
                "Fuente de poder dañada. El equipo no recibe energía.", atiende);
        System.out.println("Diagnóstico registrado.");

        casoSoporte.registrarSolucion(
                "Se reemplazó la fuente de poder original por una compatible de 90W.", atiende);
        System.out.println("Solución registrada.");

        Servicio servSoporte = catalogo.listarPorTipo(TipoAtencion.SOPORTE_TECNICO).stream()
                .filter(s -> s.getArea() == AreaTecnica.HARDWARE)
                .findFirst()
                .orElseThrow();
        casoSoporte.definirServicioYCosto(servSoporte, 20.0, atiende);
        System.out.println("Servicio definido: " + servSoporte.getNombre()
                + " | Costo: S/ " + String.format("%.2f", casoSoporte.getCosto()));

        casoSoporte.resolver(atiende);
        System.out.println("Caso resuelto. Estado: " + casoSoporte.getEstado());


        System.out.println("\n--- 7. ATENCIÓN DEL CASO DE ASESORÍA ---\n");

        Asesor atiendeAse = casoAsesoria.getAsesor();
        casoAsesoria.iniciarAtencion(atiendeAse);
        System.out.println("Atención iniciada. Estado: " + casoAsesoria.getEstado());

        casoAsesoria.registrarRecomendacion(
                "Se recomienda una laptop con procesador Intel i5 o superior, 16GB RAM, "
                        + "SSD 512GB y webcam Full HD. Modelos sugeridos: Lenovo ThinkPad E14 o Dell Latitude 5440.",
                atiendeAse);
        System.out.println("Recomendación registrada.");

        Servicio servAse = catalogo.listarPorTipo(TipoAtencion.ASESORIA).stream()
                .filter(s -> s.getArea() == AreaTecnica.HARDWARE)
                .findFirst()
                .orElseThrow();
        casoAsesoria.definirServicioYCosto(servAse, 0.0, atiendeAse);
        System.out.println("Servicio definido: " + servAse.getNombre()
                + " | Costo: S/ " + String.format("%.2f", casoAsesoria.getCosto()));

        casoAsesoria.resolver(atiendeAse);
        System.out.println("Caso resuelto. Estado: " + casoAsesoria.getEstado());

 
        System.out.println("\n--- 8. INTENTO DE CERRAR SIN PAGO ---\n");

        try {
            casoSoporte.cerrar(atiende);
        } catch (PagoPendienteException e) {
            System.out.println("RECHAZADO (esperado): " + e.getMessage());
        }

        System.out.println("\n--- 9. REGISTRO DE PAGO Y COMPROBANTE ---\n");

        Pago pago1 = cajero.cobrar(casoAsesoria, MetodoPago.YAPE);
        gestorCobranza.registrar(pago1);
        System.out.println("Pago registrado (asesoría): " + pago1);
        System.out.println("  Comprobante: " + pago1.getComprobante());
        System.out.println("  Estado del caso: " + casoAsesoria.getEstado());

        // Cliente con RUC → Factura
        Pago pago2 = cajero.cobrar(casoSoporte, MetodoPago.TRANSFERENCIA);
        gestorCobranza.registrar(pago2);
        System.out.println("Pago registrado (soporte): " + pago2);
        System.out.println("  Comprobante: " + pago2.getComprobante());
        System.out.println("  Estado del caso: " + casoSoporte.getEstado());

        System.out.println("\n--- 10. CIERRE DE CASOS ---\n");

        casoAsesoria.cerrar(atiendeAse);
        System.out.println("Caso asesoría cerrado. Estado: " + casoAsesoria.getEstado());

        casoSoporte.cerrar(atiende);
        System.out.println("Caso soporte cerrado. Estado: " + casoSoporte.getEstado());

        System.out.println("\n--- 11. CONSULTA DE SEGUIMIENTO (Cliente) ---\n");
        System.out.println(cliente1.consultarSeguimiento(casoAsesoria.getId()));

        System.out.println("--- 12. RESUMEN DE CASOS POR ESTADO ---\n");
        Map<EstadoCaso, Long> resumen = coord.resumenPorEstado(gestorCasos.listarTodos());
        resumen.forEach((estado, cantidad) ->
                System.out.println("  " + estado + ": " + cantidad));

        System.out.println("\n--- 13. CATÁLOGO DE SERVICIOS ---\n");
        catalogo.listar().forEach(s -> System.out.println("  " + s));

        System.out.println("\n--- 14. NOTIFICACIONES DEL CLIENTE ---\n");
        cliente1.getNotificaciones().forEach(n -> System.out.println("  • " + n));

        System.out.println("\n============================================================");
        System.out.println("  FIN DE LA DEMOSTRACIÓN");
        System.out.println("============================================================");
    }
}
