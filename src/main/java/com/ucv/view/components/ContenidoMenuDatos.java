package com.ucv.view.components;

import com.ucv.controller.MenuController;
import com.ucv.view.admin.FechaMenusNewUCV;

import javax.swing.*;
import java.awt.*;

public class ContenidoMenuDatos extends JPanel {
    private static final Color AZUL_TEXTO = new Color(18, 71, 150);
    private static final Color GRIS_INPUT = new Color(235, 235, 235);
    private static final Color AMARILLO_MODIFICAR = new Color(255, 210, 35);

    // --- SOLUCIÓN: CONSTRUCTOR SOBRECARGADO (5 PARÁMETROS) ---
    // Este puente salva a TarjetaMenuGestion. Como no recibe datos de la BD,
    // crea un arreglo "fantasma" con información genérica para evitar que el programa explote.
    public ContenidoMenuDatos(String tipo, String horario, String fecha, JFrame parent, MenuController controller) {
        this(tipo, horario, fecha, new String[]{
                fecha, tipo, "Plato fuerte", "Bebida", "Postre / Fruta", "0.00", "0.00", "0.00", "0", "500", "0.00"
        }, parent, controller);
    }

    // --- CONSTRUCTOR PRINCIPAL (6 PARÁMETROS) ---
    // Este es el que usa FechaMenusNewUCV con datos reales extraídos del .txt
    public ContenidoMenuDatos(String tipo, String horario, String fecha, String[] datos, JFrame parent, MenuController controller) {
        setLayout(null);
        setOpaque(false);
        setBounds(0, 0, 400, 450);

        JLabel lblTipo = new JLabel(tipo, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 45, 110));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        lblTipo.setForeground(Color.WHITE);
        lblTipo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTipo.setBounds(120, 15, 160, 30);
        add(lblTipo);

        int hReducida = 25;
        int xCampos = 40;

        // --- DATOS DINÁMICOS ---
        add(crearCampoMock(datos[2].toUpperCase(), 55, 230, hReducida, xCampos));
        add(crearCampoMock(datos[3], 85, 230, hReducida, xCampos));
        add(crearCampoMock(datos[4], 115, 230, hReducida, xCampos));

        JLabel lblHorario = new JLabel("Horario");
        lblHorario.setFont(new Font("Arial", Font.BOLD, 14));
        lblHorario.setBounds(40, 150, 100, 20);
        add(lblHorario);
        add(crearCampoMock(horario, 170, 230, hReducida, xCampos));

        // Extraemos Capacidad y CCB del modelo asegurando que no dé error si el arreglo es corto
        String capacidadReal = (datos.length > 9) ? datos[9] + " raciones" : "500 raciones";
        String ccbReal = (datos.length > 10) ? datos[10] : "0.00";

        JLabel lblDisp = new JLabel("Disponibles:");
        lblDisp.setFont(new Font("Arial", Font.BOLD, 14));
        lblDisp.setBounds(40, 210, 100, 20);
        add(lblDisp);
        add(crearCampoMock(capacidadReal, 205, 140, hReducida, 140));

        JLabel lblCCB = new JLabel("Precio neto (CCB) :");
        lblCCB.setForeground(AZUL_TEXTO);
        lblCCB.setFont(new Font("Arial", Font.BOLD, 14));
        lblCCB.setBounds(40, 250, 150, 20);
        add(lblCCB);
        add(crearCampoMock(ccbReal, 245, 90, hReducida, 180));

        JLabel bs1 = new JLabel("Bs.");
        bs1.setBounds(310, 250, 30, 20);
        add(bs1);

        JLabel lblFinal = new JLabel("Precio final comensal :");
        lblFinal.setForeground(AZUL_TEXTO);
        lblFinal.setFont(new Font("Arial", Font.BOLD, 14));
        lblFinal.setBounds(40, 285, 200, 20);
        add(lblFinal);

        String[] categorias = {"Estudiante:", "Profesor:", "Empleado:"};
        String[] precios = {datos[5], datos[6], datos[7]};

        for (int i = 0; i < 3; i++) {
            JLabel lblCat = new JLabel(categorias[i], SwingConstants.RIGHT);
            lblCat.setBounds(30, 315 + (i * 30), 100, 20);
            add(lblCat);
            add(crearCampoMock(precios[i], 310 + (i * 30), 100, hReducida, 160));
            JLabel bs = new JLabel("Bs.");
            bs.setBounds(270, 315 + (i * 30), 30, 20);
            add(bs);
        }

        PrimaryButton2 btnModificar = new PrimaryButton2("Modificar", AMARILLO_MODIFICAR);
        btnModificar.setBounds(140, 405, 120, 36);
        btnModificar.setFont(new Font("Arial", Font.BOLD, 13));
        btnModificar.setForeground(Color.BLACK);

        btnModificar.addActionListener(e -> {
            if (parent instanceof FechaMenusNewUCV) {
                FechaMenusNewUCV ventanaPadre = (FechaMenusNewUCV) parent;
                if (ventanaPadre.confirmarModificacion(tipo)) {
                    controller.irAAgregarPlato(fecha, tipo, parent);
                }
            } else {
                controller.irAAgregarPlato(fecha, tipo, parent);
            }
        });

        add(btnModificar);
    }

    private JTextField crearCampoMock(String texto, int y, int w, int h, int x) {
        JTextField f = new JTextField(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        f.setBounds(x, y, w, h);
        f.setEditable(false);
        f.setFocusable(false);
        f.setOpaque(false);
        f.setBackground(GRIS_INPUT);
        f.setForeground(new Color(70, 70, 70));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        return f;
    }
}