package com.ucv;

import com.ucv.model.ccb;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CCBCalculoTest {

    // Prueba 1: Verifica la lógica matemática en el Modelo (Metodo 1)
    @Test
    public void testEcuacionesCCB_CalculoCorrecto() {
        // Preparar datos (Costos Fijos: 100, Costos Variables: 100, Raciones: 10, Merma: 10%)
        float cf = 100f;
        float cv = 100f;
        float nb = 10f;
        float merma = 10f;

        // Ejecutar el metodo modelo
        ccb.EcuacionesCCB(cf, cv, nb, merma);
        float resultado = ccb.getCCB();

        // Verificar: ((100 + 100) / 10) * (1 + 0.10) = 20 * 1.10 = 22.0
        // JUnit 5: (esperado, actual, delta/margen_error, mensaje_si_falla)
        assertEquals(22.0f, resultado, 0.01f, "El cálculo del CCB con merma en porcentaje falló");
    }

    // Prueba 2: Verifica que el modelo maneje 0 raciones sin dividir entre cero
    @Test
    public void testEcuacionesCCB_CeroRaciones() {
        ccb.EcuacionesCCB(100f, 100f, 0f, 10f);
        assertEquals(0f, ccb.getCCB(), 0.01f, "Si las raciones (NB) son 0, el CCB debe ser 0");
    }

}