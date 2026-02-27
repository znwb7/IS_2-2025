package com.ucv.view.components;

import com.ucv.controller.MenuController;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TarjetaMenuGestion extends JPanel {
    private static final Color GRIS_TARJETA = new Color(225, 225, 225);

    public TarjetaMenuGestion(String tipo, String horario, String fecha, boolean tieneDatos, JFrame parent, MenuController controller) {
        setLayout(null);
        setOpaque(false);
        setPreferredSize(new Dimension(400, 450));

        if (tieneDatos) {
            ContenidoMenuDatos datos = new ContenidoMenuDatos(tipo, horario, fecha, parent, controller);
            datos.setBounds(0, 0, 400, 450);
            add(datos);
        } else {
            ContenidoMenuVacio vacio = new ContenidoMenuVacio(tipo, fecha, parent, controller);
            vacio.setBounds(0, 0, 400, 450);
            add(vacio);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(GRIS_TARJETA);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 40, 40));
        g2.dispose();
    }
}