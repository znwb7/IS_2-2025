package com.ucv.controller;

import com.ucv.model.DataBase;


import com.ucv.controller.UserController.Response;





public class FacialController {


    private final DataBase DataBase = new DataBase();


    public Response LoadImage() {
        // 1. Creamos el selector de archivos
        javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
        
        // Filtramos para que solo muestre imágenes
        javax.swing.filechooser.FileNameExtensionFilter filter = 
            new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG)", "jpg");
        fileChooser.setFileFilter(filter);

        int seleccion = fileChooser.showOpenDialog(null);


        //El usuario selecciona un archivo
        if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File archivoSeleccionado = fileChooser.getSelectedFile();
            String ruta = archivoSeleccionado.getAbsolutePath();

            if (!ruta.endsWith(".jpg") ) {
                return new Response(false, "Error: El archivo seleccionado no es una imagen JPG válida");
            }
            // Generamos el hash de la imagen seleccionada
            String Hash = DataBase.CreateHash(ruta);

            // Validamos si hubo error en la lectura física del archivo
            if (Hash.startsWith("Error")) {
                return new Response(false, "Error al leer el archivo de imagen");
            }

            // Buscamos el hash en la base de datos
            String hashEncontrado = DataBase.returnHash(Hash);

            if (Hash.equals(hashEncontrado)) {  
                return new Response(true, "Datos Confirmados: Acceso Permitido");
            } else {
                // RETORNO FALTANTE: Si el hash no existe en la DB
                return new Response(false, "Error: Rostro no reconocido o no registrado");
            }
        } 
        
        // El usuario cerró la ventana o presionó cancelar
        return new Response(false, "Carga de imagen cancelada por el usuario");
    }
}
