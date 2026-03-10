package com.ucv.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Enums para los estados de login y registro
public class DataBase {
    //SECCION ENUMS
        public enum LoginStatus {
            EXITO, PASSWORD_INCORRECTO, USUARIO_NO_ENCONTRADO, ARCHIVO_NO_EXISTE
        }

        public enum EnumUpdateMoney {
            SALDO_ACTUALIZADO_CON_EXITO, ERROR_AL_RECARGAR, PAGOMOVIL_NO_ENCONTRADO, ARCHIVO_NO_EXISTE, FONDO_INSUFICIENTE
        }

        public enum RegistroStatus {
            REGISTRO_EXITOSO, PERSONA_YA_EXISTENTE, USUARIO_NO_ENCONTRADO_SECRETARIA, ERROR_LECTURA_DB, FALTA_HASH_BDSECRETARIA
        }

        public enum EnumRegistrarTransaccion {
            ERROR, EXITO
        }

        public enum RolUsuario {
            ADMIN, PROFESOR, EMPLEADO, ESTUDIANTE, ERROR
        }
    //FIN

    //CONSTRUCTORES Y VARIABLES PRIVADAS
        private final String rutaArchivo;
        private final String rutaBDSecretaria;
        private final String RutaMenuDB;
        private static final String SEPARATOR = File.separator;


        // Constructor con rutas por defecto
        public DataBase() {
            this.rutaArchivo = System.getProperty("user.dir") + SEPARATOR + "target" + SEPARATOR + "Output" + SEPARATOR + "DataBase.txt";
            this.rutaBDSecretaria = System.getProperty("user.dir") + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "BaseDataSecretaria.txt";
            this.RutaMenuDB= System.getProperty("user.dir")+ SEPARATOR + "target"+ SEPARATOR + "Output"+ SEPARATOR + "MenuDB.txt";
        }

        // Constructor con rutas personalizadas (para testing o flexibilidad)
        public DataBase(String rutaArchivo, String rutaBDSecretaria, String RutaMenuDB) {
            this.rutaArchivo = rutaArchivo;
            this.rutaBDSecretaria = rutaBDSecretaria;
            this.RutaMenuDB = RutaMenuDB;
        }
    //FIN























    


    //CREACION DE CARPETAS Y ARCHIVOS DE LA BD

        private void crearArchivo() throws IOException {
            File archivo = new File(rutaArchivo);
            File directorio = archivo.getParentFile();
            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs();
            }
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        }

    //FIN

    
    //FUNCION DE REGISTRAR AL USUARIO
        
        public RegistroStatus registrar(String name, String id, String password) throws IOException {
            crearArchivo(); 
            if (findUser(id) == false) return RegistroStatus.USUARIO_NO_ENCONTRADO_SECRETARIA;

            RolUsuario rol = ObtainSecretaryRole(id);
            if (rol.name().toLowerCase().equals("error")) return RegistroStatus.ERROR_LECTURA_DB;
            String Hash = FindHash(id);
            if (Hash == null) return RegistroStatus.FALTA_HASH_BDSECRETARIA;

            if (UserAlreadyExists(id)) return RegistroStatus.PERSONA_YA_EXISTENTE;

            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
                String linea = name + " | " + id + " | " + password + " | " + rol.name().toLowerCase() + " | " + Hash + " | " + "Saldo" + " | " + "LoginActivo" + " | " + "PoseeTurnoAlmuerzo" + " | "+ "PoseeTurnoDesayuno" + " | " + "SaldoDebitoAlmuerzo" + " | " + "SaldoDebitoDesayuno" + " | " + "Fecha";
                escritor.write(linea);                                                                                        //SALDO                    LOGUEADO                TURNO ACTIVO Almuerzo   Turno ACTIVO DESAYUNO                  SALDO A DEBITAR Almuerzo         Saldo a debitar desayuno    fecha
                escritor.newLine();                                                                                             //5                          6                        7                          8                                  9                           10                              11
                return RegistroStatus.REGISTRO_EXITOSO;
            } catch (IOException e) {
                return RegistroStatus.ERROR_LECTURA_DB;
            }
        }
    
    //FIN

    //VERIFICA SI EL USUARIO YA EXISTE EN LA BD PRINCIPAL
        private boolean UserAlreadyExists(String id) throws IOException {
            File file = new File(rutaArchivo);
            if (!file.exists()) return false;

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] Word = line.split("\\s*\\|\\s*");
                    if (Word.length >= 2 && Word[1].trim().equals(id)) return true;
                }
            }
            return false;
        }
    //FIN


    //SECCION DE LOGEO

        //Comprueba los datos en la BD principal para el Login
            public LoginStatus comprobarDatos(String id, String password) throws IOException {
                File file = new File(rutaArchivo);
                if (!file.exists()) return LoginStatus.ARCHIVO_NO_EXISTE;

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] Word = line.split("\\s*\\|\\s*");
                        if (Word.length >= 4) {
                            String idEnArchivo = Word[1];
                            String passEnArchivo = Word[2];
                            if (idEnArchivo.equals(id)) {
                                return passEnArchivo.equals(password) ? LoginStatus.EXITO : LoginStatus.PASSWORD_INCORRECTO;
                            }
                        }
                    }
                }
                return LoginStatus.USUARIO_NO_ENCONTRADO;
            }

        //FIN


        //LOGEO ACTIVO
            public void LogedIn(String ID) {
                File file = new File(rutaArchivo);
                if (!file.exists()) return;

                List<String> lineasActualizadas = new ArrayList<>();

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().isEmpty()) {
                            lineasActualizadas.add(line);
                            continue;
                        }
                        String[] Word = line.split("\\s*\\|\\s*");

                        if (Word[1].equals(ID)) {
                            // Word[6] es el estado logueado (la palabra 7)
                            Word[6] = "1";
                            
                            // Reconstruir la línea
                            line = String.join(" | ", Word);
                            System.out.println("-> ¡ID encontrado! Cambiando estado a 1.");
                        }
                        lineasActualizadas.add(line);
                    }
                } catch (IOException e) {
                    System.err.println("Error al leer la base de datos: " + e.getMessage());
                    return;
                }

                // Escribir de vuelta al archivo
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                    for (String l : lineasActualizadas) {
                        bw.write(l);
                        bw.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Error al escribir: " + e.getMessage());
                }
            }
        //FIN


        //LOGEO FINALIZADO
            public void LogedOut() {
                File file = new File(rutaArchivo);
                if (!file.exists()) return;

                List<String> lineasActualizadas = new ArrayList<>();

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.trim().isEmpty()) {
                            lineasActualizadas.add(line);
                            continue;
                        }
                        String[] Word = line.split("\\s*\\|\\s*");

                        if (Word[6].equals("1")) {
                            Word[6] = "0";
                            
                            // Reconstruir la línea
                            line = String.join(" | ", Word);
                            System.out.println("-> ¡ID encontrado! Cambiando estado a 1.");
                        }
                        lineasActualizadas.add(line);
                    }
                } catch (IOException e) {
                    System.err.println("Error al leer la base de datos: " + e.getMessage());
                    return;
                }

                // Escribir de vuelta al archivo
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                    for (String l : lineasActualizadas) {
                        bw.write(l);
                        bw.newLine();
                    }
                } catch (IOException e) {
                    System.err.println("Error al escribir: " + e.getMessage());
                }
            }
        //FIN

    //FIN

    //SE ENCARGAR DE BUSCAR EL ROL DEL USUARIO EN SECRETARIA
        public RolUsuario ObtainSecretaryRole(String ID){

            File file = new File(rutaBDSecretaria);
            if (!file.exists()) return RolUsuario.ERROR;
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    // Separar por el carácter '|' (manejando espacios)
                    String[] Word = line.split("\\s*\\|\\s*");
                    if (Word[1].equals(ID)) {
                        String rol = Word[2].trim().toLowerCase();
                        switch (rol) {
                            case "admin": return RolUsuario.ADMIN;
                            case "profesor": return RolUsuario.PROFESOR;
                            case "empleado": return RolUsuario.EMPLEADO;
                            default: return RolUsuario.ESTUDIANTE;
                        }
                    }
                }
                return RolUsuario.ERROR;

            }catch( IOException e){

                return RolUsuario.ERROR;

            }
        }
    //FIN
    

    //BUSCA AL USUARIO EN LA BD DE SECRETARIA PARA CONFIRMAR EXISTENCIA
        private boolean findUser(String id) throws IOException {
            File file = new File(rutaBDSecretaria);
            if (!file.exists()) return false;

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    // Separar por el carácter '|' (manejando espacios)
                    String[] Word = line.split("\\s*\\|\\s*");
                    if (Word.length >= 2 && id.equals(Word[1].trim())) {
                        return true;
                    }
                }
            }
            return false;
        }
    //FIN


    //RETORNA EL ROL DEL USUARIO CON EL ID CORRESPONDIENTE
        public RolUsuario obtenerRol(String id) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] Word = line.split("\\s*\\|\\s*");
                if (Word.length >= 3 && Word[1].trim().equals(id)) {
                    String rol = Word[3].trim().toLowerCase();
                    System.out.println("ROL OBTENIDO: " + rol);
                    switch (rol) {
                        case "admin": return RolUsuario.ADMIN;
                        case "profesor": return RolUsuario.PROFESOR;
                        case "empleado": return RolUsuario.EMPLEADO;
                        default: return RolUsuario.ESTUDIANTE;
                    }
                }
            }
        }
        return RolUsuario.ESTUDIANTE;
        }
    //FIN


    //SECCION DE TRABAJO DEL HASH 

        //CREA EL HASH DE LA IMAGEN QUE SE LE PASE
        public String CreateHash(String ruta) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                try (InputStream is = Files.newInputStream(Paths.get(ruta))) {
                    byte[] buffer = new byte[1024];
                    int leido;
                    while ((leido = is.read(buffer)) != -1) {
                        md.update(buffer, 0, leido);
                    }
                }
                byte[] digest = md.digest();
                StringBuilder SBuilder = new StringBuilder();
                for (byte b : digest) {
                    SBuilder.append(String.format("%02x", b));
                }
                return SBuilder.toString(); 
            } catch (Exception e) {
                return "Error al procesar la imagen: ";
            }
        }
        
        //BUSCA EL HASH EN LA BDSECRETARIA PARA ADJUNTARLO A LA BD PRINCIPAL
        private String FindHash(String ID){
            try (BufferedReader br = new BufferedReader(new FileReader(rutaBDSecretaria))) {
                String Line;
                while ((Line = br.readLine()) != null) {
                    String[] Word = Line.split("\\s*\\|\\s*");
                    if (Line.isEmpty()) continue;
                    if (Word[1].equals(ID)) {
                        return Word[3]; 
                    }
                } 
                return null; 
            } catch (IOException e) { 
                return null; 
            }
        }

        //RETORNA EL HASH DE LA BD PRINCIPAL
        public String returnHash(String Hash){
            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                String Line;
                while ((Line = br.readLine()) != null) {
                    String[] Word = Line.split("\\s*\\|\\s*");
                    if (Line.isEmpty()) continue;
                    if (Word[4].equals(Hash)) {
                        return Word[4]; 
                    }
                } 
                return null; 
            } catch (IOException e) { 
                return null; 
            }
        }
    //FIN



    //SECCION DE ACTUALIZACION DEL SALDO DEL USUARIO

        //ACTUALIZA EL SALDO DEL USUARIO EN LA BD PRINCIPAL
            public EnumUpdateMoney UpdateMoney(String ID, String montoASumar) {

                File archivo = new File(rutaArchivo);
                if (!archivo.exists()) return EnumUpdateMoney.ARCHIVO_NO_EXISTE;
                List<String> lineasActualizadas = new ArrayList<>();
                boolean usuarioEncontrado = false;
                try {
                    double monto = Double.parseDouble(montoASumar.trim().replace(',', '.'));
                    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {

                        String linea;
                        while ((linea = br.readLine()) != null) {

                            if (linea.trim().isEmpty()) {
                                lineasActualizadas.add(linea);
                                continue;
                            }

                            String[] Word = linea.split("\\s*\\|\\s*");
                            if (Word[1].equals(ID)) {

                                usuarioEncontrado = true;
                                double saldoActual = Double.parseDouble(Word[5]);
                                double nuevoSaldo = saldoActual + monto;
                                nuevoSaldo = Math.round(nuevoSaldo * 100.0) / 100.0;

                                linea = Word[0] + " | " + Word[1] + " | " + Word[2] + " | " + Word[3] + " | " + Word[4] + " | " + nuevoSaldo + " | " + Word[6] + " | " + Word[7] + " | " + Word[8]+ " | " + Word[9]+ " | " + Word[10]+ " | " + Word[11];
                            }

                            lineasActualizadas.add(linea);
                        }
                    }

                    if (!usuarioEncontrado)
                        return EnumUpdateMoney.PAGOMOVIL_NO_ENCONTRADO;

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, false))) {
                        for (String l : lineasActualizadas) {
                            bw.write(l);
                            bw.newLine();
                        }
                    }

                    return EnumUpdateMoney.SALDO_ACTUALIZADO_CON_EXITO;

                } catch (Exception e) {
                    e.printStackTrace();
                    return EnumUpdateMoney.ERROR_AL_RECARGAR;
                }
            }
        //FIN


        //EXTRAE EL SALDO DEL USUARIO EN LA BD PRINCIPAL
            public EnumUpdateMoney ExtractMoney(String ID, String montoARestar) {
                File archivo = new File(rutaArchivo);
                if (!archivo.exists()) return EnumUpdateMoney.ARCHIVO_NO_EXISTE;

                List<String> lineasPrincipal = new ArrayList<>();
                boolean usuarioEncontrado = false;
                boolean saldoSuficiente = true;

                try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        if (linea.trim().isEmpty()) continue;
                        String[] WordP = linea.split("\\s*\\|\\s*");

                        if (WordP[1].equals(ID)) {
                            usuarioEncontrado = true;
                            
                            // 1. Conversion montos a double para la operación
                            double saldoActual = Double.parseDouble(WordP[5].trim());
                            double cantidadARestar = Double.parseDouble(montoARestar.trim().replace(',', '.'));

                            // 2. Verificación de saldo
                            if (saldoActual >= cantidadARestar) {
                                double nuevoSaldo = saldoActual - cantidadARestar;

                                // 3. Redondeo a 2 decimales
                                nuevoSaldo = Math.round(nuevoSaldo * 100.0) / 100.0;

                                // 4. Reconstruccion de la línea con el nuevo saldo
                               linea = WordP[0] + " | " + WordP[1] + " | " + WordP[2] + " | " + WordP[3] + " | " + WordP[4] + " | " + nuevoSaldo + " | " + WordP[6] + " | " + WordP[7] + " | " + WordP[8]+ " | " + WordP[9]+ " | " + WordP[10]+ " | " + WordP[11];
                            } else {
                                saldoSuficiente = false;
                            }
                        }
                        lineasPrincipal.add(linea);
                    }
                } catch (IOException | NumberFormatException e) {
                    return EnumUpdateMoney.ERROR_AL_RECARGAR;
                }

                if (!usuarioEncontrado) return EnumUpdateMoney.PAGOMOVIL_NO_ENCONTRADO;
                if (!saldoSuficiente) return EnumUpdateMoney.FONDO_INSUFICIENTE; 

                //Volcamos los datos actualizados al archivo
                try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, false))) {
                    for (String l : lineasPrincipal) {
                        escritor.write(l);
                        escritor.newLine();
                    }
                    return EnumUpdateMoney.SALDO_ACTUALIZADO_CON_EXITO;
                } catch (IOException e) {
                    return EnumUpdateMoney.ERROR_AL_RECARGAR;
                }
            }
        //FIN

    //FIN 
    



    //RETORNA LA CEDULA DEL USUARIO ACTIVO
    public String ReturnID() {
        File file = new File(rutaArchivo);
        if (!file.exists()) return "ERROR_ARCHIVO_NO_EXISTE";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Separamos la línea por el delimitador '|'
                String[] Word = line.split("\\s*\\|\\s*");

                // Verificamos que la línea tenga al menos 6 columnas (índice 5 es la sexta)
                // y que el valor en el índice 5 sea "1"
                if (Word[6].equals("1")) {
                    // Retornamos Word[2] que es la cedula (índice 1)
                    return Word[1]; 
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR al leer la base de datos de secretaria: " + e.getMessage());
            return "ERROR_LECTURA";
        }

        return "ERROR_NADIE_CONECTADO"; // Si recorre todo el archivo y no hay ningún "1"
    }
   
    //BANDERAS DEL MENU

        public void MenuDesayunoActive(String ID) {
            SetDateDB(ID);
            File file = new File(rutaArchivo);
            if (!file.exists()) return;

            List<String> lineasActualizadas = new ArrayList<>();

            // 1. Fase de Lectura y Modificación
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        lineasActualizadas.add(line);
                        continue;
                    }
                    String[] Word = line.split("\\s*\\|\\s*");

                    if (Word[1].equals(ID)) {
                        Word[8] = "1"; // Modifica la palabra 9 (índice 8)
                        line = String.join(" | ", Word);
                        System.out.println("-> ¡ID encontrado! Cambiando estado de turno a 1.");
                    }
                    lineasActualizadas.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error al leer la base de datos: " + e.getMessage());
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) { // 'false' para sobrescribir
                for (String l : lineasActualizadas) {
                    bw.write(l);
                    bw.newLine();
                }
                System.out.println("-> Base de datos actualizada correctamente.");
            } catch (IOException e) {
                System.err.println("Error al escribir en la base de datos: " + e.getMessage());
            }
        }

        public void MenuDesayunoOut(String ID) {
            File file = new File(rutaArchivo);
            if (!file.exists()) return;

            List<String> lineasActualizadas = new ArrayList<>();

            // 1. Fase de Lectura y Modificación
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        lineasActualizadas.add(line);
                        continue;
                    }
                    String[] Word = line.split("\\s*\\|\\s*");

                    if (Word[1].equals(ID)) {
                        Word[8] = "0"; // Modifica la palabra 9 (índice 8)
                        line = String.join(" | ", Word);
                        System.out.println("-> ¡ID encontrado! Cambiando estado de turno a 1.");
                    }
                    lineasActualizadas.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error al leer la base de datos: " + e.getMessage());
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) { // 'false' para sobrescribir
                for (String l : lineasActualizadas) {
                    bw.write(l);
                    bw.newLine();
                }
                System.out.println("-> Base de datos actualizada correctamente.");
            } catch (IOException e) {
                System.err.println("Error al escribir en la base de datos: " + e.getMessage());
            }
        }

        //Activa el booleano que dice si posee un menu activo o no Almuerzo
        public void MenuAlmuerzoActive(String ID) {
            SetDateDB(ID);
            File file = new File(rutaArchivo);
            if (!file.exists()) return;

            List<String> lineasActualizadas = new ArrayList<>();

            // 1. Fase de Lectura y Modificación
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        lineasActualizadas.add(line);
                        continue;
                    }
                    String[] Word = line.split("\\s*\\|\\s*");

                    if (Word[1].equals(ID)) {
                        Word[7] = "1"; // Modifica la palabra 8 (índice 7)
                        line = String.join(" | ", Word);
                        System.out.println("-> ¡ID encontrado! Cambiando estado de turno a 1.");
                    }
                    lineasActualizadas.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error al leer la base de datos: " + e.getMessage());
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) { // 'false' para sobrescribir
                for (String l : lineasActualizadas) {
                    bw.write(l);
                    bw.newLine();
                }
                System.out.println("-> Base de datos actualizada correctamente.");
            } catch (IOException e) {
                System.err.println("Error al escribir en la base de datos: " + e.getMessage());
            }
        }
        
        //Desactiva el bool de posesion de menu Almuerzo
        public void MenuAlmuerzoOut(String ID) {
            File file = new File(rutaArchivo);
            if (!file.exists()) return;

            List<String> lineasActualizadas = new ArrayList<>();

            // 1. Fase de Lectura y Modificación
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        lineasActualizadas.add(line);
                        continue;
                    }
                    String[] Word = line.split("\\s*\\|\\s*");

                    if (Word[1].equals(ID)) {
                        Word[7] = "0"; // Modifica la palabra 8 (índice 7)
                        line = String.join(" | ", Word);
                        System.out.println("-> ¡ID encontrado! Cambiando estado de turno a 1.");
                    }
                    lineasActualizadas.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error al leer la base de datos: " + e.getMessage());
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) { // 'false' para sobrescribir
                for (String l : lineasActualizadas) {
                    bw.write(l);
                    bw.newLine();
                }
                System.out.println("-> Base de datos actualizada correctamente.");
            } catch (IOException e) {
                System.err.println("Error al escribir en la base de datos: " + e.getMessage());
            }
        }

        public String GetFoodDesayunoFlag(String ID){

            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                String Line;
                while ((Line = br.readLine()) != null) {
                    String[] Word = Line.split("\\s*\\|\\s*");
                    if (Line.isEmpty()) continue;
                    if (Word[1].equals(ID)) {
                        return Word[8]; 
                    }
                } 
                return "0"; 
            } catch (IOException e) { 
                return "0"; 
            }
        }

        public String GetFoodAlmuerzoFlag(String ID){

            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                String Line;
                while ((Line = br.readLine()) != null) {
                    String[] Word = Line.split("\\s*\\|\\s*");
                    if (Line.isEmpty()) continue;
                    if (Word[1].equals(ID)) {
                        return Word[7]; 
                    }
                } 
                return "0"; 
            } catch (IOException e) { 
                return "0"; 
            }
        }
    
    //FIN


    //OBTENCION DEL SALDO
        public double obtenerSaldo(String usuarioID) {
            File file = new File(rutaArchivo);
            if (!file.exists()) return 0;

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] Word = line.split("\\s*\\|\\s*");

                    if (Word[1].trim().equals(usuarioID)) {
                        return Double.parseDouble(Word[5].trim());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0;
        }
    //FIN

    //Pone los precios de la comida en la db espacio especifico del usuario
        
      public EnumRegistrarTransaccion PrecioComida(String id, String tipo) {
            try {
                RolUsuario rol = obtenerRol(id);

                // Si es ADMIN o hubo un ERROR, no se registra gasto (no hay precio para ellos)
                if (rol == RolUsuario.ADMIN || rol == RolUsuario.ERROR) {
                    return EnumRegistrarTransaccion.ERROR; 
                }

                String montoAPagar = "0";
                File fileMenu = new File(RutaMenuDB);
                if (!fileMenu.exists()) return EnumRegistrarTransaccion.ERROR;

                // 1. Buscar el precio en MenuDB.txt (Tipo en pos 1, Estudiante 5, Profesor 6, Empleado 7)
                try (BufferedReader brMenu = new BufferedReader(new FileReader(fileMenu))) {
                    String line;
                    while ((line = brMenu.readLine()) != null) {
                        if (line.trim().isEmpty()) continue;
                        String[] wordMenu = line.split("\\s*\\|\\s*");
                        
                        // Verificamos el tipo de comida en el índice 1 (segunda columna)
                        if (wordMenu.length > 1 && wordMenu[1].equalsIgnoreCase(tipo)) {
                            switch (rol) {
                                case ESTUDIANTE: montoAPagar = wordMenu[5]; break;
                                case PROFESOR:   montoAPagar = wordMenu[6]; break;
                                case EMPLEADO:   montoAPagar = wordMenu[7]; break;
                                default: montoAPagar = "0"; break;
                            }
                            break;
                        }
                    }
                }

                // 2. Actualizar la posición 8 (columna 9) en DataBase.txt
                File filePrincipal = new File(rutaArchivo);
                List<String> lineasActualizadas = new ArrayList<>();
                boolean usuarioEncontrado = false;

                try (BufferedReader brPrincipal = new BufferedReader(new FileReader(filePrincipal))) {
                    String linea;
                    while ((linea = brPrincipal.readLine()) != null) {
                        if (linea.trim().isEmpty()) {
                            lineasActualizadas.add(linea);
                            continue;
                        }
                        String[] wordsP = linea.split("\\s*\\|\\s*");
                        //SALDO A DEBITAR DESAYUNO
                        if (wordsP[1].equals(id) && tipo.equals("desayuno")) {
                            usuarioEncontrado = true;
                            // Actualizamos el "Saldo a debitar" 
                            wordsP[10] = montoAPagar; 
                            linea = String.join(" | ", wordsP);
                        }
                        //ALMUERZO
                        if (wordsP[1].equals(id) && tipo.equals("almuerzo")) {
                            usuarioEncontrado = true;
                            // Actualizamos el "Saldo a debitar"
                            wordsP[9] = montoAPagar; 
                            linea = String.join(" | ", wordsP);
                        }
                        lineasActualizadas.add(linea);
                    }
                }

                if (!usuarioEncontrado) return EnumRegistrarTransaccion.ERROR;

                // 3. Sobrescribir archivo principal
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePrincipal, false))) {
                    for (String l : lineasActualizadas) {
                        bw.write(l);
                        bw.newLine();
                    }
                }

                return EnumRegistrarTransaccion.EXITO;

            } catch (IOException e) {
                e.printStackTrace();
                return EnumRegistrarTransaccion.ERROR;
            }
        }
    //FIN

    //ESTABLECE LA FECHA Y ES LLAMADO CUANDO SE LLAMA A CUALQUIERA DE LOS DOS MENUS

        private void SetDateDB(String ID){
             File file = new File(rutaArchivo);
            if (!file.exists()) return;

            List<String> lineasActualizadas = new ArrayList<>();

            // 1. Fase de Lectura y Modificación
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        lineasActualizadas.add(line);
                        continue;
                    }
                    String[] Word = line.split("\\s*\\|\\s*");

                    if (Word[1].equals(ID)) {
                        Word[11] = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));; // Modifica la palabra 9 (índice 8)
                        line = String.join(" | ", Word);
                        System.out.println("-> ¡ID encontrado! Cambiando fecha a " + Word[11]);
                    }
                    lineasActualizadas.add(line);
                }
            } catch (IOException e) {
                System.err.println("Error al leer la base de datos: " + e.getMessage());
                return;
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) { // 'false' para sobrescribir
                for (String l : lineasActualizadas) {
                    bw.write(l);
                    bw.newLine();
                }
                System.out.println("-> Base de datos actualizada correctamente.");
            } catch (IOException e) {
                System.err.println("Error al escribir en la base de datos: " + e.getMessage());
            }




        }


}
