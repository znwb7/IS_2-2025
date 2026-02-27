package com.ucv.view.components;

import com.ucv.controller.MenuController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ContenidoMenuVacio extends JPanel {

    private Color colorCirculo = new Color(200, 200, 200);
    private final Color COLOR_HOVER = new Color(170, 170, 170);
    private final Color COLOR_NORMAL = new Color(200, 200, 200);

    public ContenidoMenuVacio(String tipo, String fecha, JFrame parent, MenuController controller) {
        setLayout(new GridBagLayout());
        setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel("<html>Agregar <b>" + tipo + "</b></html>");
        label.setFont(new Font("Arial", Font.PLAIN, 28));
        label.setForeground(new Color(50, 50, 50));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 40, 0);
        add(label, gbc);

        JButton btnPlus = new JButton("+") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(colorCirculo);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btnPlus.setPreferredSize(new Dimension(180, 180));
        btnPlus.setFont(new Font("Arial", Font.PLAIN, 100));
        btnPlus.setForeground(Color.WHITE);
        btnPlus.setContentAreaFilled(false);
        btnPlus.setBorderPainted(false);
        btnPlus.setFocusPainted(false);
        btnPlus.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPlus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                colorCirculo = COLOR_HOVER;
                btnPlus.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                colorCirculo = COLOR_NORMAL;
                btnPlus.repaint();
            }
        });

        btnPlus.addActionListener(e -> {
            controller.irAAgregarPlato(fecha, tipo, parent);
        });

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(btnPlus, gbc);
    }
}