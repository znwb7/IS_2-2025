package com.ucv.model;

public class ccb {

    private static float CCB = 0;

    public static void EcuacionesCCB (float CF, float CV, float NB, float Merma){
        if (NB == 0f) {
            CCB = 0f;
            return;
        }
        // Si Merma viene como porcentaje (por ejemplo 5 para 5%), convertir a fracción
        if (Merma > 1f) {
            Merma = Merma / 100f;
        }
        // Cálculo neto del CCB (sin ajustes por rol)
        CCB = ((CF + CV)/NB) * (1 + Merma);
    }

    public static float getCCB (){
        return CCB;
    }
}
