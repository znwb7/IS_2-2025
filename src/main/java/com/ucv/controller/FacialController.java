package com.ucv.controller;

import com.ucv.model.DataBase;
import com.ucv.model.DataBase.RolUsuario;
import com.ucv.controller.UserController.Response;
import java.time.LocalTime;

public class FacialController {

    private final DataBase DataBase = new DataBase();

    private String obtenerTipoComida() {

    LocalTime horaActual = LocalTime.now();

    LocalTime inicioDesayuno = LocalTime.of(7,0);
    LocalTime finDesayuno = LocalTime.of(11,0);

    LocalTime inicioAlmuerzo = LocalTime.of(12,0);
    LocalTime finAlmuerzo = LocalTime.of(17,0);

    if (!horaActual.isBefore(inicioDesayuno) && !horaActual.isAfter(finDesayuno)) {
        return "desayuno";
    }

    if (!horaActual.isBefore(inicioAlmuerzo) && !horaActual.isAfter(finAlmuerzo)) {
        return "almuerzo";
    }

    return "fuera_horario";
}

public Response LoadImage(String userID) {

    javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();

    javax.swing.filechooser.FileNameExtensionFilter filter =
            new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG)", "jpg");

    fileChooser.setFileFilter(filter);

    int seleccion = fileChooser.showOpenDialog(null);

    if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {

        java.io.File archivoSeleccionado = fileChooser.getSelectedFile();
        String ruta = archivoSeleccionado.getAbsolutePath();

        if (!ruta.endsWith(".jpg")) {
            return new Response(false, "Error: El archivo seleccionado no es una imagen JPG válida");
        }

        String hashImagen = DataBase.CreateHash(ruta);

        if (hashImagen.startsWith("Error")) {
            return new Response(false, "Error al leer el archivo de imagen");
        }

        String hashDB = DataBase.getHashByID(userID);

        if (hashDB == null) {
            return new Response(false, "Error: Usuario no encontrado");
        }

        if (!hashImagen.equals(hashDB)) {
            return new Response(false, "Error: Rostro no coincide con el usuario");
        }

        // -------------------------
        // VERIFICAR HORARIO
        // -------------------------

        String tipoComida = obtenerTipoComida();

        if (tipoComida.equals("fuera_horario")) {
            return new Response(false, "No estamos en horario de comida");
        }

        // -------------------------
        // VERIFICAR RESERVACIÓN
        // -------------------------

        if (tipoComida.equals("desayuno")) {

            if (!DataBase.GetFoodDesayunoFlag(userID).equals("1")) {
                return new Response(false, "No posee reservación de desayuno");
            }

        } else {

            if (!DataBase.GetFoodAlmuerzoFlag(userID).equals("1")) {
                return new Response(false, "No posee reservación de almuerzo");
            }
        }

        // -------------------------
        // OBTENER ROL DEL USUARIO
        // -------------------------

        RolUsuario rolUsuario;

        try {
            rolUsuario = DataBase.obtenerRol(userID);
        } catch (java.io.IOException e) {
            return new Response(false, "Error al obtener rol del usuario");
        }

        // -------------------------
        // USUARIO EXONERADO
        // -------------------------

        if (rolUsuario == RolUsuario.EXONERADO) {

            if (tipoComida.equals("desayuno")) {
                DataBase.MenuDesayunoOut(userID);
            }

            if (tipoComida.equals("almuerzo")) {
                DataBase.MenuAlmuerzoOut(userID);
            }

            return new Response(true, "Acceso permitido (usuario exonerado).");
        }

        // -------------------------
        // USUARIO NORMAL (SE COBRA)
        // -------------------------

        double saldoUsuario = DataBase.obtenerSaldo(userID);

        double costo = tipoComida.equals("desayuno")
                ? DataBase.getPrecioDesayuno(userID)
                : DataBase.getPrecioAlmuerzo(userID);

        if (saldoUsuario < costo) {
            return new Response(false, "Saldo insuficiente para pagar la comida");
        }

        com.ucv.model.DataBase.EnumUpdateMoney resultadoCobro =
                DataBase.ExtractMoney(userID, String.valueOf(costo));

        if (resultadoCobro != com.ucv.model.DataBase.EnumUpdateMoney.SALDO_ACTUALIZADO_CON_EXITO) {
            return new Response(false, "Error al procesar el pago");
        }

        // -------------------------
        // DESACTIVAR TURNO
        // -------------------------

        if (tipoComida.equals("desayuno")) {
            DataBase.MenuDesayunoOut(userID);
        }

        if (tipoComida.equals("almuerzo")) {
            DataBase.MenuAlmuerzoOut(userID);
        }

        return new Response(true, "Acceso permitido. Comida registrada.");
    }

    return new Response(false, "Carga de imagen cancelada por el usuario");
}
}
