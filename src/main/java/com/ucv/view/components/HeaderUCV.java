package com.ucv.view.components;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HeaderUCV extends JPanel {

    private final Color AZUL_ENCABEZADO = new Color(10, 45, 110);

    public HeaderUCV() {
        // --- AJUSTE 1: Reducción de altura de 150 a 130 ---
        setPreferredSize(new Dimension(0, 130));
        setOpaque(false);
        setLayout(null);

        // 1. Lógica de Fecha
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaTexto = fechaActual.format(formatter);

        // 2. TÍTULO Y FECHA (Ajuste de coordenadas Y para la nueva altura)
        JLabel lblTitulo = new JLabel("Comedor UCV");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 60)); // Reduje un poco el tamaño (70 -> 60)
        lblTitulo.setBounds(40, 5, 600, 70); // Subimos Y de 15 a 5

        JLabel lblFecha = new JLabel(fechaTexto);
        lblFecha.setForeground(new Color(210, 210, 210));
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 20));
        lblFecha.setBounds(45, 70, 200, 30); // Subimos Y de 85 a 70

        // 3. LOGO (Ajuste de tamaño y posición)
        JLabel lblLogo = new JLabel();
        try {
            java.net.URL res = getClass().getResource("/com/ucv/view/logo_comedor.png");
            if (res != null) {
                // Reducimos el logo de 120 a 105 para que quepa en la nueva altura
                Image img = new ImageIcon(res).getImage()
                        .getScaledInstance(105, 105, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {}

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                // Posicionamos el logo centrado verticalmente en los 130px
                lblLogo.setBounds(getWidth() - 150, 10, 105, 105);
            }
        });

        add(lblTitulo);
        add(lblFecha);
        add(lblLogo);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- AJUSTE 2: Altura del dibujo azul ---
        int alto = 125; // El dibujo termina en 125 para dejar 5px de aire
        int arc = 50;   // Arco ligeramente menos pronunciado

        g2.setColor(AZUL_ENCABEZADO);

        // Dibujamos la base azul corregida
        g2.fillRoundRect(-30, 0, getWidth() + 60, alto, arc, arc);

        // Rellenamos la mitad superior
        g2.fillRect(-30, 0, getWidth() + 60, alto / 2);

        g2.dispose();
    }
}