package com.ucv;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ucv.controller.UserController;
import com.ucv.controller.UserController.Response;

/**
 * Prueba unitaria para el flujo de registro de usuarios.
 * 
 * IDs de prueba predefinidos:
 * - VALID_SECRETARIA_NUEVO: Existe en BaseDataSecretaria.txt, NO en DataBase.txt
 * - INVALID_SECRETARIA: NO existe en BaseDataSecretaria.txt  
 * - EXISTENTE_DUPLICADO: Ya existe en DataBase.txt
 */

public class UserRegistrationTest {

    private UserController controller;

    // IDs constantes para reproducibilidad
    private static final String ID_VALIDO_NUEVO = "27444333"; 
    private static final String ID_INVALIDO = "99999999";       
    private static final String ID_DUPLICADO = "31983764";    

    @BeforeEach
    public void setUp() {
         File db = new File(System.getProperty("user.dir")
            + File.separator + "target"
            + File.separator + "Output"
            + File.separator + "DataBase.txt");

    if (db.exists()) {
        db.delete();
    }
        controller = new UserController();
    }


    @Test
    public void testRegistroExitoso() {
        // GIVEN: Datos válidos (ID existe en secretaría pero NO en DB local)
        String nombre = "Usuario ";
        String apellido = "Prueba Nuevo";
        String clave = "password123";

        // WHEN: Intentamos registrar
        Response response = controller.register(nombre, apellido, ID_VALIDO_NUEVO, clave);

        // THEN: Registro exitoso con mensaje correcto
        assertAll("Registro exitoso",
            () -> assertTrue(response.isSuccess(), "Debe registrar usuario nuevo"),
            () -> assertEquals("Usuario registrado correctamente", response.getMessage())
        );
    }

    @Test
    public void testRegistroIdNoExistenteEnSecretaria() {
        // GIVEN: ID inválido (no existe en secretaría)
        Response response = controller.register("Invalido", " ", ID_INVALIDO, "clave");

        // THEN: Rechazo por ID no válido
        assertAll("ID no existe en secretaría",
            () -> assertFalse(response.isSuccess()),
            () -> assertEquals("El ID no existe en la base de la secretaría", response.getMessage())
        );
    }

    @Test
    public void testRegistroUsuarioDuplicado() {
    // GIVEN: primero registramos el usuario
    controller.register("Juan ", "Original", ID_DUPLICADO, "clave1");

    // WHEN: intentamos registrarlo otra vez
    Response response = controller.register("Juan ","Duplicado", ID_DUPLICADO, "otraClave");

    // THEN: debe detectar duplicado
    assertAll("Usuario duplicado",
        () -> assertFalse(response.isSuccess()),
        () -> assertEquals("El usuario ya está registrado", response.getMessage())
    );
}

}
