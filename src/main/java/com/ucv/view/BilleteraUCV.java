package com.ucv.view;

import com.ucv.model.DataBase;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar; // Cambiado a SideBar
import com.ucv.view.components.PrimaryButton2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class BilleteraUCV extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color AMARILLO_BOTON = new Color(255, 210, 35);

    public BilleteraUCV() {
        setTitle("Mi Saldo · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Contenedor Principal
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // Cabecera y Barra Lateral (Actualizada a SideBar)
        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SideBar(this), BorderLayout.WEST);

        // Panel Central para posicionar la tarjeta del saldo
        JPanel panelCentral = new JPanel(null);
        panelCentral.setOpaque(false);

        // --- ETIQUETA "Saldo Disponible" ---
        JLabel lblTitulo = new JLabel("Saldo Disponible:");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(360, 160, 300, 40);
        panelCentral.add(lblTitulo);

        // --- TARJETA DE SALDO ---
        PanelRedondeado tarjetaSaldo = new PanelRedondeado(30, GRIS_TARJETA);
        tarjetaSaldo.setBounds(350, 200, 700, 220);
        tarjetaSaldo.setLayout(null);

        // Monto del Saldo
        JLabel lblMonto = new JLabel("Bs 0.00");
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblMonto.setForeground(Color.BLACK);
        lblMonto.setBounds(50, 70, 400, 60);
        tarjetaSaldo.add(lblMonto);

        // Botón Agregar Saldo
        PrimaryButton2 btnAgregar = new PrimaryButton2("Agregar saldo", AMARILLO_BOTON);
        btnAgregar.setBounds(500, 140, 160, 45);
        btnAgregar.setForeground(Color.BLACK);
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAgregar.addActionListener(e -> {
            new PagoMovilUCV().setVisible(true);
            dispose();
        });
        tarjetaSaldo.add(btnAgregar);

        panelCentral.add(tarjetaSaldo);
        container.add(panelCentral, BorderLayout.CENTER);

        // Footer con el nuevo código y listener funcional
        container.add(crearFooter(), BorderLayout.SOUTH);

        add(container);

        this.revalidate();
        this.repaint();
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 40));
        JLabel cerrarSesion = new JLabel("<html><u>Cerrar Sesión</u></html>");
        cerrarSesion.setForeground(Color.WHITE);
        cerrarSesion.setFont(new Font("Arial", Font.PLAIN, 20));
        cerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                DataBase dataBase = new DataBase();
                dataBase.LogedOut();
                dispose();
            }
        });
        footer.add(cerrarSesion);
        return footer;
    }

    static class PanelRedondeado extends JPanel {
        private int radio;
        private Color color;
        public PanelRedondeado(int radio, Color color) {
            this.radio = radio; this.color = color;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BilleteraUCV().setVisible(true));
    }
}