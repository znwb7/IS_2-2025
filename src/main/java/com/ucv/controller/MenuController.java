package com.ucv.controller;

import com.ucv.model.MenuDB;
import com.ucv.view.*;
import com.ucv.view.admin.AdminUCV;
import com.ucv.view.admin.AgregarPlatoUCV;
import com.ucv.view.admin.FechaMenusNewUCV;
import com.ucv.view.admin.GestionMenuUCV;
import com.ucv.view.user.ConfirmacionReserva;

import javax.swing.JFrame;
import java.io.IOException;

public class MenuController {

    private final MenuDB menuDB;

    public MenuController() {
        this.menuDB = new MenuDB();
    }

    // --- NUEVO MÉTODO PUENTE (RESPETA EL MVC) ---
    // Permite a las vistas pedir datos sin tocar el Modelo directamente
    public String[] obtenerDatosMenu(String fecha, String tipo) {
        return menuDB.obtenerMenu(fecha, tipo);
    }

    public void procesarFechaSeleccionada(String fecha, JFrame vistaActual) {
        String[] datosDesayuno = menuDB.obtenerMenu(fecha, "desayuno");
        String[] datosAlmuerzo = menuDB.obtenerMenu(fecha, "almuerzo");

        if (vistaActual != null) vistaActual.dispose();
        new FechaMenusNewUCV(fecha, datosDesayuno, datosAlmuerzo, this).setVisible(true);
    }

    public void irAAgregarPlato(String fecha, String tipo, JFrame vistaActual) {
        if (vistaActual != null) vistaActual.dispose();
        new AgregarPlatoUCV(fecha, tipo, this).setVisible(true);
    }

    public void registrarNuevoPlato(String fecha, String tipo, String plato, String bebida, String postre,
                                    String pEst, String pProf, String pEmp, String capacidad, String ccb, JFrame vistaActual) {
        try {
            boolean existe = menuDB.consultarExistencia(fecha, tipo);

            MenuDB.WriteMenuStatus status = menuDB.WriteMenu(
                    existe, false, fecha, tipo, plato, bebida, postre, pEst, pProf, pEmp, capacidad, ccb);

            if (status == MenuDB.WriteMenuStatus.REGISTRO_EXITOSO || status == MenuDB.WriteMenuStatus.ACTUALIZACION_EXITOSA) {
                if (vistaActual != null) vistaActual.dispose();
                procesarFechaSeleccionada(fecha, null);
            } else {
                javax.swing.JOptionPane.showMessageDialog(vistaActual, "Error al guardar el menú: " + status);
            }
        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(vistaActual, "Error crítico de archivo: " + e.getMessage());
        }
    }

    public void volverAGestionMenu(JFrame vistaActual) {
        if (vistaActual != null) vistaActual.dispose();
        new GestionMenuUCV(this).setVisible(true);
    }

    public void volverAAdmin(JFrame vistaActual) {
        if (vistaActual != null) vistaActual.dispose();
        new AdminUCV("Administrador").setVisible(true);
    }

    public void irAConfirmacionReserva(String tipo, String usuarioID, JFrame vistaActual) {
        if (vistaActual != null) vistaActual.dispose();
        new ConfirmacionReserva(tipo, usuarioID).setVisible(true);
    }
}