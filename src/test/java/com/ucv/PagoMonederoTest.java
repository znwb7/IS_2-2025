package com.ucv;

import com.ucv.controller.PagoController;
import com.ucv.model.PagoModel;
import com.ucv.model.PagoModel.ResultadoValidacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PagoMonederoTest {

    private PagoModel pagoModel;
    private PagoController pagoController;

    @BeforeEach
    public void setUp() {
        pagoModel = new PagoModel();
        // Inicializamos el controlador con una cédula ficticia para aislar la prueba
        pagoController = new PagoController(pagoModel, "Ficticio123");
    }

    // --- METODO 1 A EVALUAR: validarYProcesarPago (de la clase PagoModel) ---
    @Test
    public void testValidarYProcesarPago_BloqueoDeDatosInvalidos() {
        // Caso A: Intento de recargar el monedero con 0 Bolívares
        ResultadoValidacion resultadoCero = pagoModel.validarYProcesarPago("27123456", "0.00", "12345678");
        assertEquals(ResultadoValidacion.CAMPOS_INVALIDOS, resultadoCero,
                "El modelo debe rechazar montos en cero o negativos para el monedero.");

        // Caso B: Intento de usar una referencia con letras (el regex exige solo números)
        ResultadoValidacion resultadoRefInvalida = pagoModel.validarYProcesarPago("27123456", "100.00", "REF12345");
        assertEquals(ResultadoValidacion.CAMPOS_INVALIDOS, resultadoRefInvalida,
                "El modelo debe rechazar referencias alfanuméricas.");

        // Caso C: Intento de enviar datos nulos (simulando un fallo grave en la vista)
        ResultadoValidacion resultadoNulo = pagoModel.validarYProcesarPago(null, "100.00", "12345678");
        assertEquals(ResultadoValidacion.CAMPOS_INVALIDOS, resultadoNulo,
                "El modelo debe manejar parámetros nulos de forma segura sin lanzar NullPointerException.");
    }

    // --- METODO 2 A EVALUAR: obtenerSaldo (de la clase PagoController) ---
    @Test
    public void testObtenerSaldo_ManejoDeUsuarioInexistente() {
        // Al consultar el saldo del usuario "Ficticio123", la base de datos lanzará una excepción.
        // El método obtenerSaldo() debe atrapar este error en su bloque try-catch y devolver 0.0
        double saldo = pagoController.obtenerSaldo();

        // En JUnit 5 los valores double requieren un margen de tolerancia (0.001)
        assertEquals(0.0, saldo, 0.001,
                "El controlador debe retornar saldo 0.0 si el usuario no existe o la DB falla.");
    }
}