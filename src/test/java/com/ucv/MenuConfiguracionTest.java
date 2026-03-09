package com.ucv;

import com.ucv.model.MenuDB;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MenuConfiguracionTest {

    // Prueba 1: Verifica la escritura y lectura de la Base de Datos (Metodo de Configuración)
    @Test
    public void testCicloDeVidaConfiguracionMenu() {
        MenuDB menuDB = new MenuDB();
        String fechaPrueba = "99/99/9999"; // Fecha imposible para no chocar con menús reales
        String tipoPrueba = "almuerzo";

        // Usamos assertDoesNotThrow de JUnit 5 para asegurar que no ocurran errores de IO
        assertDoesNotThrow(() -> {
            // 1. Ejecutar Escritura (WriteMenu)
            MenuDB.WriteMenuStatus estadoEscritura = menuDB.WriteMenu(
                    false, false, fechaPrueba, tipoPrueba,
                    "Pabellon Criollo", "Jugo de Mora", "Galleta",
                    "15.00", "20.00", "25.00", "500", "30.00"
            );

            // Verificar que se guardó correctamente (JUnit 5: condicion, mensaje)
            assertTrue(
                    estadoEscritura == MenuDB.WriteMenuStatus.REGISTRO_EXITOSO ||
                            estadoEscritura == MenuDB.WriteMenuStatus.ACTUALIZACION_EXITOSA,
                    "El menú no se pudo registrar en el archivo"
            );

            // 2. Ejecutar Lectura (obtenerMenu)
            String[] datosRecuperados = menuDB.obtenerMenu(fechaPrueba, tipoPrueba);

            // Verificar que el arreglo no sea nulo
            assertNotNull(datosRecuperados, "No se pudo recuperar el menú recién guardado");

            // --- CORRECCIÓN AQUÍ ---
            // Ahora verificamos que conserve el formato Capitalizado en lugar de forzar minúsculas
            assertEquals(fechaPrueba, datosRecuperados[0], "La fecha no coincide");
            assertEquals("Pabellon Criollo", datosRecuperados[2], "El plato fuerte no conservó las mayúsculas/formato original");
            assertEquals("500", datosRecuperados[9], "La capacidad no coincide");
            assertEquals("30.00", datosRecuperados[10], "El CCB no coincide");

        }, "Se produjo una excepción inesperada de entrada/salida durante la prueba");
    }

}