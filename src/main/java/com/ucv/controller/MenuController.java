package com.ucv.controller;

import com.ucv.model.MenuDB;
import com.ucv.view.admin.AdminUCV;
import com.ucv.view.admin.AgregarPlatoUCV;
import com.ucv.view.admin.FechaMenusNewUCV;
import com.ucv.view.admin.GestionMenuUCV;
import com.ucv.view.user.ConfirmacionReserva;

import javax.swing.JFrame;

public class MenuController {

    private final MenuDB menuDB;

    public MenuController() {
        this.menuDB = new MenuDB();
    }

    public String[] obtenerDatosMenu(String fecha, String tipo) {
        return menuDB.obtenerMenu(fecha, tipo);
    }

    public void procesarFechaSeleccionada(String fecha, JFrame vistaActual) {
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(java.time.format.ResolverStyle.STRICT);

            java.time.LocalDate fechaIngresada = java.time.LocalDate.parse(fecha, formatter);
            java.time.LocalDate hoy = java.time.LocalDate.now();

            if (fechaIngresada.isBefore(hoy)) {
                javax.swing.JOptionPane.showMessageDialog(vistaActual, "No se pueden registrar ni modificar menús de fechas pasadas.", "Fecha Inválida", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (java.time.format.DateTimeParseException e) {
            javax.swing.JOptionPane.showMessageDialog(vistaActual, "La fecha seleccionada no es válida en el calendario (Ej. 31 de febrero).", "Fecha Inexistente", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

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
                                    String pEst, String pProf, String pEmp, String capacidad, String ccb,
                                    String tarifaEst, String tarifaProf, String tarifaEmp, JFrame vistaActual) {

        try {
            float tEst = Float.parseFloat(tarifaEst.replace(",", "."));
            float tProf = Float.parseFloat(tarifaProf.replace(",", "."));
            float tEmp = Float.parseFloat(tarifaEmp.replace(",", "."));

            StringBuilder erroresRangos = new StringBuilder();

            if (tEst < 20f || tEst > 30f) erroresRangos.append("La tarifa de Estudiante debe estar entre 20% y 30%.\n");
            if (tProf < 70f || tProf > 90f) erroresRangos.append("La tarifa de Profesor debe estar entre 70% y 90%.\n");
            if (tEmp < 90f || tEmp > 110f) erroresRangos.append("La tarifa de Empleado debe estar entre 90% y 110%.\n");

            if (erroresRangos.length() > 0) {
                javax.swing.JOptionPane.showMessageDialog(vistaActual, erroresRangos.toString().trim(), "Rango Inválido", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(vistaActual, "Las tarifas deben ser valores numéricos válidos.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        String platoFormat = capitalizarPalabras(plato);
        String bebidaFormat = capitalizarPalabras(bebida);
        String postreFormat = capitalizarPalabras(postre);

        try {
            boolean existe = menuDB.consultarExistencia(fecha, tipo);
            MenuDB.WriteMenuStatus status = menuDB.WriteMenu(
                    existe, false, fecha, tipo, platoFormat, bebidaFormat, postreFormat, pEst, pProf, pEmp, capacidad, ccb);

            if (status == MenuDB.WriteMenuStatus.REGISTRO_EXITOSO || status == MenuDB.WriteMenuStatus.ACTUALIZACION_EXITOSA) {
                if (vistaActual != null) vistaActual.dispose();
                procesarFechaSeleccionada(fecha, null);
            } else {
                javax.swing.JOptionPane.showMessageDialog(vistaActual, "Error al guardar el menú: " + status);
            }
        } catch (java.io.IOException e) {
            javax.swing.JOptionPane.showMessageDialog(vistaActual, "Error crítico de archivo: " + e.getMessage());
        }
    }

    private String capitalizarPalabras(String texto) {
        if (texto == null || texto.trim().isEmpty() || texto.equals("N/A")) return texto;
        String[] palabras = texto.trim().split("\\s+");
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(Character.toUpperCase(palabra.charAt(0)))
                        .append(palabra.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return resultado.toString().trim();
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

    // --- LÓGICA DE PROCESAMIENTO DE RESERVA CORREGIDA ---
    public boolean procesarReserva(String tipo, String usuarioID) {
        String fechaActual = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // 1. Verificar si el usuario ya reservó ESTE TIPO de menú hoy (Desayuno y Almuerzo son independientes)
        if (yaReservoEsteMenu(usuarioID, fechaActual, tipo)) {
            return false;
        }

        // 2. Verificar si hay cupos disponibles en el menú
        if (menuDB.CantidadDisponible(tipo) <= 0) {
            return false;
        }

        // 3. Procesar: Aumentar el contador en MenuDB.txt (+1)
        MenuDB.ReWriteStatus status = menuDB.CountMenu(fechaActual, tipo);

        if (status == MenuDB.ReWriteStatus.REWRITE_EXITOSO) {
            // 4. Guardar registro local para evitar doble reserva del MISMO plato
            registrarReservaLocal(usuarioID, fechaActual, tipo);
            return true;
        }

        return false;
    }

    // --- MÉTODOS AUXILIARES DE CONTROL DE RESERVAS ---
    private boolean yaReservoEsteMenu(String usuarioID, String fecha, String tipo) {
        java.io.File archivo = new java.io.File(System.getProperty("user.dir") + java.io.File.separator + "target" + java.io.File.separator + "Output" + java.io.File.separator + "ControlReservas.txt");
        if (!archivo.exists()) return false;

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Compara el ID, la fecha y el tipo de menú (ej. "12345678|09/03/2026|desayuno")
                if (linea.equals(usuarioID + "|" + fecha + "|" + tipo.toLowerCase())) {
                    return true;
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    private void registrarReservaLocal(String usuarioID, String fecha, String tipo) {
        java.io.File archivo = new java.io.File(System.getProperty("user.dir") + java.io.File.separator + "target" + java.io.File.separator + "Output" + java.io.File.separator + "ControlReservas.txt");
        try {
            if (!archivo.exists()) {
                archivo.getParentFile().mkdirs();
                archivo.createNewFile();
            }
            try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(archivo, true))) {
                bw.write(usuarioID + "|" + fecha + "|" + tipo.toLowerCase());
                bw.newLine();
            }
        } catch (Exception ignored) {}
    }
}