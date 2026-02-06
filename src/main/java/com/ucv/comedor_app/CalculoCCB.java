package com.ucv.comedor_app;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculoCCB extends JFrame {

    // Componentes de la interfaz
    private JTextField TipoUsuario, CF, CV, NB, Merma;
    private JButton boton;
    private float PAlumno = 25 , PTrabajador = 100 , PProfesor= 80, CCB = 0;

    public CalculoCCB() {
        // Configuración básica
        setTitle("Captura de Datos");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Un layout sencillo de 5 filas para los campos y 1 para el botón
        setLayout(new GridLayout(6, 1, 10, 10));

        // Inicializar los campos
        add(new JLabel(" Tipo de Usuario:"));
        TipoUsuario = new JTextField(); add(TipoUsuario);

        add(new JLabel(" CF:"));
        CF = new JTextField(); add(CF);

         add(new JLabel(" CV:"));
        CV = new JTextField(); add(CV);

         add(new JLabel(" NB:"));
        NB = new JTextField(); add(NB);

         add(new JLabel(" Merma:"));
        Merma = new JTextField(); add(Merma);

        boton = new JButton("Asignar a Variables");
        add(boton);
        
        LogicaBoton();
        // Centrar en pantalla y mostrar
        setLocationRelativeTo(null);
        setVisible(true);
    }


    // Lógica del botón

    void LogicaBoton(){
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                
                // ASIGNACIÓN A VARIABLES STRING
                String UType = TipoUsuario.getText();
                String s3 = CF.getText();
                String s4 = CV.getText();
                String s5 = NB.getText();
                String s6 = Merma.getText();
                
                Float CF = Float.parseFloat(s3);
                Float CV = Float.parseFloat(s4);
                Float NB = Float.parseFloat(s5);
                Float Merma = Float.parseFloat(s6);

                EcuacionesCCB(UType, CF, CV, NB, Merma);

                // Demostración de que están guardadas
                System.out.println("Variables capturadas:");
                System.out.println( " CCB de " + UType +" " + CCB);
                System.out.println("1: " + UType + " | 2: |"  + " | 3: " + CF + " | 4: " + CV + " | 5: " + NB + " | 6: " + Merma);
                CCB = 0;
            }
        });
    }

    void SetPAlumno (int NewP){

        PAlumno = NewP;
    }

    void SetPTrabajador(int NewP){

        PTrabajador = NewP;
    }

    void SetPProfesor(int NewP){

        PProfesor = NewP;
    }

    void EcuacionesCCB (String UType, float CF, float CV, float NB, float Merma){

        if (UType.equals("Estudiante")){
            CCB = ((CF + CV)/NB) * (1 + Merma);
            CCB = (CCB * PAlumno) /100f;

        }else if (UType.equals("Profesor")){
            CCB = ((CF + CV)/NB) * (1 + Merma);
            CCB = (CCB * PProfesor) /100f;

        }else if (UType.equals("Trabajador")){
            CCB = ((CF + CV)/NB) * (1 + Merma);
            CCB = (CCB * PTrabajador) /100f;

        }
    }

    float getCCB (){
        return CCB;
    }

    public static void main(String[] args) {
        new CalculoCCB();
    }
}