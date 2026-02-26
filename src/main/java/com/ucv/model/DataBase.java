package com.ucv.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

// Enums para los estados de login y registro
public class DataBase {

    public enum LoginStatus {
        EXITO, PASSWORD_INCORRECTO, USUARIO_NO_ENCONTRADO, ARCHIVO_NO_EXISTE
    }

    public enum UpdateMoney {
        SALDO_ACTUALIZADO_CON_EXITO, ERROR_AL_RECARGAR, PAGOMOVIL_NO_ENCONTRADO, ARCHIVO_NO_EXISTE, FONDO_INSUFICIENTE
    }

    public enum RegistroStatus {
        REGISTRO_EXITOSO, PERSONA_YA_EXISTENTE, USUARIO_NO_ENCONTRADO_SECRETARIA, ERROR_LECTURA_DB, FALTA_HASH_BDSECRETARIA
    }

    public enum RolUsuario {
        ADMIN, SECRETARIA, ESTUDIANTE, ERROR
    }

    private final String rutaArchivo;
    private final String rutaPagoMovil;
    private final String rutaBDSecretaria;
    private static final String SEPARATOR = File.separator;

    // Constructor con rutas por defecto
    public DataBase() {
        this.rutaArchivo = System.getProperty("user.dir") + SEPARATOR + "target" + SEPARATOR + "Output" + SEPARATOR + "DataBase.txt";
        this.rutaBDSecretaria = System.getProperty("user.dir") + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "BaseDataSecretaria.txt";
        this.rutaPagoMovil = System.getProperty("user.dir") + SEPARATOR + "src" + SEPARATOR + "main" + SEPARATOR + "resources" + SEPARATOR + "DataBasePagos.txt";
    }

    // Constructor con rutas personalizadas (para testing o flexibilidad)
    public DataBase(String rutaArchivo, String rutaBDSecretaria, String rutaPagoMovil) {
        this.rutaArchivo = rutaArchivo;
        this.rutaBDSecretaria = rutaBDSecretaria;
        this.rutaPagoMovil = rutaPagoMovil;
    }

  
    public static void main(String[] args) {
        DataBase db = new DataBase();
        String idPrueba = "31983764";

        System.out.println("=== PRUEBA DE RETURNID (BUSCAR USUARIO ACTIVO) ===");

        try {
            // PASO 1: Asegurarnos de que el usuario tenga el "1" en la secretaría
            // Nota: Asegúrate de que tu método LogedIn esté apuntando a rutaBDSecretaria para esta prueba
            System.out.println("-> Paso 1: Marcando estado activo en Secretaria para ID: " + idPrueba);
            db.LogedIn(idPrueba); 

            // PASO 2: Ejecutar ReturnID
            System.out.println("-> Paso 2: Ejecutando ReturnID()...");
            String idEncontrado = db.ReturnID();

            if (idEncontrado.startsWith("ERROR")) {
                System.err.println("-> Resultado fallido: " + idEncontrado);
            } else {
                System.out.println("-> ¡Éxito! El ID del usuario activo es: " + idEncontrado);
            }

        } catch (Exception e) {
            System.err.println("Error en la prueba: " + e.getMessage());
        }
    }

    //SE USA EN EL LOGIN, ENCARGADA DE BUSCAR LOS DATOS DEL USUARIO PARA ACCEDER AL MENU
    public LoginStatus comprobarDatos(String id, String password) throws IOException {
        File file = new File(rutaArchivo);
        if (!file.exists()) return LoginStatus.ARCHIVO_NO_EXISTE;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] partes = line.split("\\s*\\|\\s*");
                if (partes.length >= 4) {
                    String idEnArchivo = partes[1];
                    String passEnArchivo = partes[2];
                    if (idEnArchivo.equals(id)) {
                        return passEnArchivo.equals(password) ? LoginStatus.EXITO : LoginStatus.PASSWORD_INCORRECTO;
                    }
                }
            }
        }
        return LoginStatus.USUARIO_NO_ENCONTRADO;
    }

    //SE ENCARGAR DE BUSCAR EL ROL DEL USUARIO EN SECRETARIA
    public RolUsuario obtenerRolSecretaria(String ID){

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
                        case "secretaria": return RolUsuario.SECRETARIA;
                        case "estudiante" : return RolUsuario.ESTUDIANTE;
                        default: return RolUsuario.ERROR;
                    }
                }
            }
            return RolUsuario.ERROR;

        }catch( IOException e){

            return RolUsuario.ERROR;

        }
    }
    
    //SE ENCARGA DE REGISTRAR AL USUARIO EN LA BD PRINCIPAL
    public RegistroStatus registrar(String name, String id, String password) throws IOException {
        crearArchivo(); 
        if (findUser(id) == false) return RegistroStatus.USUARIO_NO_ENCONTRADO_SECRETARIA;

        RolUsuario rol = obtenerRolSecretaria(id);
        if (rol.name().toLowerCase().equals("error")) return RegistroStatus.ERROR_LECTURA_DB;
        String Hash = FindHash(id);
        if (Hash == null) return RegistroStatus.FALTA_HASH_BDSECRETARIA;

        if (usuarioYaExiste(id)) return RegistroStatus.PERSONA_YA_EXISTENTE;

        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, true))) {
            String linea = name + " | " + id + " | " + password + " | " + rol.name().toLowerCase() + " | " + Hash + " | " + "0" + " | " + "0" + " | " + "0";
            escritor.write(linea);                                                                                        //SALDO       LOGUEADO   TURNO ACTIVO
            escritor.newLine();                                                                                             //5            6            7
            return RegistroStatus.REGISTRO_EXITOSO;
        } catch (IOException e) {
            return RegistroStatus.ERROR_LECTURA_DB;
        }
    }

    //CREA LAS CARPETAS Y ARCHIVOS CORRESPONDIENTES PARA LA BD
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

    //VERIFICA SI EL USUARIO YA EXISTE EN LA BD PRINCIPAL
    private boolean usuarioYaExiste(String id) throws IOException {
        File file = new File(rutaArchivo);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] partes = line.split("\\s*\\|\\s*");
                if (partes.length >= 2 && partes[1].trim().equals(id)) return true;
            }
        }
        return false;
    }

    //BUSCA AL USUARIO EN LA BD DE SECRETARIA PARA CONFIRMAR EXISTENCIA
    private boolean findUser(String id) throws IOException {
        File file = new File(rutaBDSecretaria);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // Separar por el carácter '|' (manejando espacios)
                String[] partes = line.split("\\s*\\|\\s*");
                if (partes.length >= 2 && id.equals(partes[1].trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    //ES UN GETROL
    public RolUsuario obtenerRol(String id) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] partes = line.split("\\s*\\|\\s*");
            if (partes.length >= 3 && partes[1].trim().equals(id)) {
                String rol = partes[3].trim().toLowerCase();
                System.out.println("ROL OBTENIDO: " + rol);
                switch (rol) {
                    case "admin": return RolUsuario.ADMIN;
                    case "secretaria": return RolUsuario.SECRETARIA;
                    default: return RolUsuario.ESTUDIANTE;
                }
            }
        }
    }
    return RolUsuario.ESTUDIANTE;
}

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

    //ACTUALIZA EL SALDO DEL USUARIO EN LA BD PRINCIPAL
    public UpdateMoney UpdateMoney(String ID, String Fecha) {

        File archivo = new File(rutaPagoMovil);
        if (!archivo.exists()) return UpdateMoney.ARCHIVO_NO_EXISTE;
        List<String> lineas = new ArrayList<>();

        boolean DaDaCo = false;
        double MontoEncontrado = 0; // Para guardar el monto del pago

        // 1. Buscamos el monto en la base de datos de Pagos
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] Word = linea.split("\\s*\\|\\s*");
                if (Word.length >= 6 && Word[1].equals(ID) && Word[5].equals(Fecha)) {
                    MontoEncontrado = Double.parseDouble(Word[3]);
                    DaDaCo = true;
                }
                lineas.add(linea);
            }
        } catch (IOException e) {
            return UpdateMoney.ARCHIVO_NO_EXISTE;
        }

        // 2. Si se encontró el pago, actualizamos la BD principal (rutaArchivo)
        if (DaDaCo) {
            List<String> lineasPrincipal = new ArrayList<>();
            File filePrincipal = new File(rutaArchivo);
            
            try (BufferedReader br = new BufferedReader(new FileReader(filePrincipal))) {
                String lineaP;
                while ((lineaP = br.readLine()) != null) {
                    String[] WordP = lineaP.split("\\s*\\|\\s*");
                    
                    // Si encontramos al usuario por ID en la BD Principal
                    if (WordP.length >= 6 && WordP[1].equals(ID)) {
                        MontoEncontrado = MontoEncontrado + Double.parseDouble(WordP[5]);
                        MontoEncontrado = Math.round(MontoEncontrado * 100.0) / 100.0;
                        // Reconstruimos la línea: Nombre | ID | Pass | Rol | Hash | Monto
                        String Monto = String.valueOf(MontoEncontrado);
                        lineaP = WordP[0] + " | " + WordP[1] + " | " + WordP[2] + " | " + WordP[3] + " | " + WordP[4] + " | " + Monto;
                    }
                    lineasPrincipal.add(lineaP);
                }
            } catch (IOException e) {
                return UpdateMoney.ERROR_AL_RECARGAR;
            }

            // 3. Escribimos los cambios de vuelta en la BD Principal
            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, false))) {
                for (String l : lineasPrincipal) {
                    escritor.write(l);
                    escritor.newLine();
                }
                return UpdateMoney.SALDO_ACTUALIZADO_CON_EXITO;
            } catch (IOException e) {
                return UpdateMoney.ERROR_AL_RECARGAR;
            }
        }

        return UpdateMoney.PAGOMOVIL_NO_ENCONTRADO;
    }

    //EXTRAE EL SALDO DEL USUARIO EN LA BD PRINCIPAL
    public UpdateMoney ExtractMoney(String ID, String montoARestar) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) return UpdateMoney.ARCHIVO_NO_EXISTE;

        List<String> lineasPrincipal = new ArrayList<>();
        boolean usuarioEncontrado = false;
        boolean saldoSuficiente = true;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] WordP = linea.split("\\s*\\|\\s*");

                if (WordP.length >= 6 && WordP[1].equals(ID)) {
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
                        linea = WordP[0] + " | " + WordP[1] + " | " + WordP[2] + " | " + WordP[3] + " | " + WordP[4] + " | " + nuevoSaldo;
                    } else {
                        saldoSuficiente = false;
                    }
                }
                lineasPrincipal.add(linea);
            }
        } catch (IOException | NumberFormatException e) {
            return UpdateMoney.ERROR_AL_RECARGAR;
        }

        if (!usuarioEncontrado) return UpdateMoney.PAGOMOVIL_NO_ENCONTRADO;
        if (!saldoSuficiente) return UpdateMoney.FONDO_INSUFICIENTE; 

        // 5. Volcamos los datos actualizados al archivo
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo, false))) {
            for (String l : lineasPrincipal) {
                escritor.write(l);
                escritor.newLine();
            }
            return UpdateMoney.SALDO_ACTUALIZADO_CON_EXITO;
        } catch (IOException e) {
            return UpdateMoney.ERROR_AL_RECARGAR;
        }
    }

    //DEBE CAMBIAR EL ESTADO DE UN BOOLEANO EN LA BD PRINCIPAL PARA IDENTIFICAR EL LOGEO ACTIVO
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

    //CAMBIA EL ESTADO DEL BOOL DE 1 A 0 PARA EL DESLOGEO
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
  
    //RETORNA LA CEDULA DEL USUARIO ACTIVO
    public String ReturnID() {
        File file = new File(rutaArchivo);
        if (!file.exists()) return "ERROR_ARCHIVO_NO_EXISTE";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Separamos la línea por el delimitador '|'
                String[] partes = line.split("\\s*\\|\\s*");

                // Verificamos que la línea tenga al menos 6 columnas (índice 5 es la sexta)
                // y que el valor en el índice 5 sea "1"
                if (partes[6].equals("1")) {
                    // Retornamos Word[2] que es la cedula (índice 1)
                    return partes[1]; 
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR al leer la base de datos de secretaria: " + e.getMessage());
            return "ERROR_LECTURA";
        }

        return "ERROR_NADIE_CONECTADO"; // Si recorre todo el archivo y no hay ningún "1"
    }
   
    //Activa el booleano que dice si posee un menu activo o no
    public void MenuActive(String ID) {
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
    
    //Desactiva el bool de posesion de menu
    public void MenuOut(String ID) {
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



    public String GetFoodFlag(String ID){

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

}
