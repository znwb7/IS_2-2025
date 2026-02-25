package com.ucv.view.components;

import javax.swing.*;
import java.awt.*;

public class ContenidoMenuDatos extends JPanel {
    private static final Color AZUL_TEXTO = new Color(18, 71, 150);
    private static final Color GRIS_INPUT = new Color(235, 235, 235);
    private static final Color AMARILLO_MODIFICAR = new Color(255, 210, 35);

    public ContenidoMenuDatos(String tipo, String horario, String fecha, JFrame parent) {
        setLayout(null);
        setOpaque(false);
        setBounds(0, 0, 400, 450); // Tamaño estándar de la tarjeta

        // --- Título del Panel (Desayuno/Almuerzo) ---
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

        // --- Campos de Comida ---
        int hReducida = 25;
        int xCampos = 40;
        add(crearCampoMock("Plato fuerte", 55, 230, hReducida, xCampos));
        add(crearCampoMock("Bebida", 85, 230, hReducida, xCampos));
        add(crearCampoMock("Postre / Fruta", 115, 230, hReducida, xCampos));

        // --- Horario ---
        JLabel lblHorario = new JLabel("Horario");
        lblHorario.setFont(new Font("Arial", Font.BOLD, 14));
        lblHorario.setBounds(40, 150, 100, 20);
        add(lblHorario);
        add(crearCampoMock(horario, 170, 230, hReducida, xCampos));

        // --- Disponibles ---
        JLabel lblDisp = new JLabel("Disponibles:");
        lblDisp.setFont(new Font("Arial", Font.BOLD, 14));
        lblDisp.setBounds(40, 210, 100, 20);
        add(lblDisp);
        add(crearCampoMock("500 raciones", 205, 140, hReducida, 140));

        // --- Precios ---
        JLabel lblCCB = new JLabel("Precio neto (CCB) :");
        lblCCB.setForeground(AZUL_TEXTO);
        lblCCB.setFont(new Font("Arial", Font.BOLD, 14));
        lblCCB.setBounds(40, 250, 150, 20);
        add(lblCCB);
        add(crearCampoMock("0.00", 245, 90, hReducida, 180));

        JLabel bs1 = new JLabel("Bs.");
        bs1.setBounds(310, 250, 30, 20);
        add(bs1);

        JLabel lblFinal = new JLabel("Precio final comensal :");
        lblFinal.setForeground(AZUL_TEXTO);
        lblFinal.setFont(new Font("Arial", Font.BOLD, 14));
        lblFinal.setBounds(40, 285, 200, 20);
        add(lblFinal);

        String[] categorias = {"Estudiante:", "Profesor:", "Empleado:"};
        for (int i = 0; i < 3; i++) {
            JLabel lblCat = new JLabel(categorias[i], SwingConstants.RIGHT);
            lblCat.setBounds(30, 315 + (i * 30), 100, 20);
            add(lblCat);
            add(crearCampoMock("0.00", 310 + (i * 30), 100, hReducida, 160));
            JLabel bs = new JLabel("Bs.");
            bs.setBounds(270, 315 + (i * 30), 30, 20);
            add(bs);
        }

        // --- Botón Modificar ---
        PrimaryButton2 btnModificar = new PrimaryButton2("Modificar", AMARILLO_MODIFICAR);
        btnModificar.setBounds(140, 405, 120, 36);
        btnModificar.setFont(new Font("Arial", Font.BOLD, 13));
        btnModificar.setForeground(Color.BLACK);
        btnModificar.addActionListener(e -> {
            parent.dispose();
            // Aquí llamarías a tu vista de AgregarPlato
            // new AgregarPlatoUCV(fecha, tipo).setVisible(true);
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