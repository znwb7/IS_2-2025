package com.ucv.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SIdeBar2 extends JPanel {

    private final Color GRIS_CLARO = new Color(225, 225, 225);

    // Constructor simplificado: ya no requiere el booleano esAdmin
    public SIdeBar2(JFrame ventanaActual) {

        setPreferredSize(new Dimension(100, 0));
        setOpaque(false);
        setLayout(null);

        PanelRedondeado capsula = new PanelRedondeado(30, GRIS_CLARO);
        capsula.setBounds(15, 250, 65, 75);
        capsula.setLayout(new BorderLayout());

        JLabel home = new JLabel("", SwingConstants.CENTER);
        try {
            java.net.URL res = getClass().getResource("/com/ucv/view/home.png");
            if (res != null) {
                Image img = new ImageIcon(res).getImage()
                        .getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                home.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {}

        home.setCursor(new Cursor(Cursor.HAND_CURSOR));
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ventanaActual.dispose();
                // REDIRECCIÓN ÚNICA A ADMIN
                new com.ucv.view.admin.AdminUCV("Administrador").setVisible(true);
            }
        });

        capsula.add(home, BorderLayout.CENTER);
        add(capsula);
    }

    static class PanelRedondeado extends JPanel {
        private final int radio;
        private final Color color;

        public PanelRedondeado(int radio, Color color) {
            this.radio = radio;
            this.color = color;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radio, radio));
            g2.dispose();
        }
    }
}