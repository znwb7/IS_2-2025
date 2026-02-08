import javax.swing.*;

public class CalculoCCB extends JFrame {
    // Componentes de la interfaz

    private static float PAlumno = 25 , PTrabajador = 100 , PProfesor= 80, CCB = 0;

    public static void SetPAlumno (int NewP){

        PAlumno = NewP;
    }

    public static void SetPTrabajador(int NewP){

        PTrabajador = NewP;
    }

    public static void SetPProfesor(int NewP){

        PProfesor = NewP;
    }

    public static void EcuacionesCCB (String UType, float CF, float CV, float NB, float Merma, int Promedio){

        CCB = ((CF + CV)/NB) * (1 + Merma);
        CCB = (CCB * Promedio) /100f;
    }

    public static float getCCB (){
        return CCB;
    }

    public static void main(String[] args) {
    }
}
