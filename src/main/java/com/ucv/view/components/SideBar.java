package com.ucv.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SideBar extends JPanel {

    private final Color GRIS_CLARO = new Color(225, 225, 225);
    private final String usuarioID;

    // Constructor principal (recomendado)
    public SideBar(JFrame ventanaActual, String usuarioID) {
        this.usuarioID = usuarioID;
        inicializar(ventanaActual);
    }

    // Constructor opcional para compatibilidad (NO recomendado para sesión real)
    public SideBar(JFrame ventanaActual) {
        this.usuarioID = null;
        inicializar(ventanaActual);
    }

    private void inicializar(JFrame ventanaActual) {

        setPreferredSize(new Dimension(90, 0));
        setOpaque(false);
        setLayout(null);

        PanelRedondeado capsula = new PanelRedondeado(30, GRIS_CLARO);
        capsula.setBounds(15, 230, 60, 180);
        capsula.setLayout(new GridLayout(2, 1, 0, 20));

        // ---------- HOME ----------
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
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ventanaActual.dispose();

                if (usuarioID != null) {
                    new com.ucv.view.user.MenusUsuario(usuarioID).setVisible(true);
                } else {
                    new com.ucv.view.user.MenusUsuario("Usuario").setVisible(true);
                }
            }
        });

        // ---------- BILLETERA ----------
        JLabel billetera = new JLabel("", SwingConstants.CENTER);
        try {
            java.net.URL resB = getClass().getResource("/com/ucv/view/billetera.png");
            if (resB != null) {
                Image img = new ImageIcon(resB).getImage()
                        .getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                billetera.setIcon(new ImageIcon(img));
            }
        } catch (Exception ignored) {}

        billetera.setCursor(new Cursor(Cursor.HAND_CURSOR));
        billetera.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                ventanaActual.dispose();
                    new com.ucv.view.user.BilleteraUCV(usuarioID).setVisible(true);
        }    });

        capsula.add(home);
        capsula.add(billetera);
        add(capsula);
    }

    // ---------------- PANEL REDONDEADO ----------------
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