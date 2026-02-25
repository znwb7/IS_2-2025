package com.ucv.view.components;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HeaderUCV extends JPanel {

    private final Color AZUL_ENCABEZADO = new Color(10, 45, 110);

    public HeaderUCV() {

        setPreferredSize(new Dimension(0, 150));
        setOpaque(false);
        setLayout(null);

        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaTexto = fechaActual.format(formatter);

        JLabel lblTitulo = new JLabel("Comedor UCV");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 70));
        lblTitulo.setBounds(40, 15, 600, 80);

        JLabel lblFecha = new JLabel(fechaTexto);
        lblFecha.setForeground(new Color(210, 210, 210));
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 22));
        lblFecha.setBounds(45, 85, 200, 30);

        JLabel lblLogo = new JLabel();
        try {
            java.net.URL res = getClass().getResource("/com/ucv/view/logo_comedor.png");
            if (res != null) {
                Image img = new ImageIcon(res).getImage()
                        .getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {}

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                lblLogo.setBounds(getWidth() - 140, 15, 110, 110);
            }
        });

        add(lblTitulo);
        add(lblFecha);
        add(lblLogo);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int alto = 140;
        int arc = 60;

        g2.setColor(AZUL_ENCABEZADO);
        g2.fillRoundRect(-30, 0, getWidth() + 60, alto, arc, arc);
        g2.fillRect(-30, 0, getWidth() + 60, alto / 2);
        g2.fillRect(-30, 0, 100, alto);
    }
}