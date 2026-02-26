package com.ucv.view.components;

import com.ucv.view.ConfirmacionReserva;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TarjetaMenuUsuario extends JPanel {
    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color AZUL_DISPONIBLES = new Color(20, 35, 100);
    private static final Color AMARILLO_BOTON = new Color(255, 210, 35);
    private final String usuarioID;

    public TarjetaMenuUsuario(String tipo, String horario, String maxCupos, String precio, boolean disponible, String usuarioID) {
        this.usuarioID = usuarioID;
        setPreferredSize(new Dimension(420, 370));
        setOpaque(false);

        if (!disponible) {
            setLayout(new GridBagLayout());
            JLabel lblNoDisp = new JLabel("No disponible");
            lblNoDisp.setFont(new Font("Segoe UI", Font.BOLD, 36));
            lblNoDisp.setForeground(new Color(100, 100, 100)); // Gris para estado inactivo
            add(lblNoDisp);
        } else {
            setLayout(null);
            armarInterfazDisponible(tipo, horario, maxCupos, precio);
        }
    }

    private void armarInterfazDisponible(String tipo, String horario, String maxCupos, String precio) {
        // --- Título (Almuerzo/Desayuno) ---
        JLabel lblTipo = new JLabel(tipo, SwingConstants.CENTER);
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTipo.setBounds(0, 25, 420, 40);
        add(lblTipo);

        // --- Detalle del menú ---
        JLabel lblCuerpo = new JLabel("<html><div style='line-height: 85%;'><b>Plato fuerte</b><br>Bebida<br>Postre / Fruta</div></html>");
        lblCuerpo.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblCuerpo.setBounds(50, 75, 320, 80);
        add(lblCuerpo);

        // --- Horario ---
        JLabel lblHorarioV = new JLabel("Horario: " + horario);
        lblHorarioV.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblHorarioV.setBounds(50, 170, 300, 25);
        add(lblHorarioV);

        // --- Caja de Disponibilidad (Caja Azul) ---
        PanelRedondeadoSimple cajaAzul = new PanelRedondeadoSimple(20, AZUL_DISPONIBLES);
        cajaAzul.setBounds(50, 210, 320, 70);
        cajaAzul.setLayout(null);

        JLabel lblDispT = new JLabel("Disponibles:");
        lblDispT.setForeground(Color.WHITE);
        lblDispT.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDispT.setBounds(20, 10, 200, 25);
        cajaAzul.add(lblDispT);

        JLabel lblDispV = new JLabel("XXX / " + maxCupos + " comensales");
        lblDispV.setForeground(Color.WHITE);
        lblDispV.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblDispV.setBounds(20, 37, 280, 25);
        cajaAzul.add(lblDispV);
        add(cajaAzul);

        // --- Precio ---
        JLabel lblBs = new JLabel("Bs " + precio);
        lblBs.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblBs.setBounds(50, 300, 150, 45);
        add(lblBs);

        // --- Botón Seleccionar con Redirección ---
        PrimaryButton2 btnSel = new PrimaryButton2("Seleccionar", AMARILLO_BOTON);
        btnSel.setBounds(215, 300, 165, 45);
        btnSel.setForeground(Color.BLACK);
        btnSel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnSel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSel.addActionListener(e -> {
            // Obtenemos la ventana (JFrame) que contiene a esta tarjeta
            Window ventanaPadre = SwingUtilities.getWindowAncestor(this);
            if (ventanaPadre != null) {
                ventanaPadre.dispose(); // Cerramos la ventana de Menus
            }
            // Abrimos la confirmación pasando el tipo de menú y el ID del usuario
            new ConfirmacionReserva(tipo, this.usuarioID).setVisible(true);
        });
        add(btnSel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(GRIS_TARJETA);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        g2.dispose();
    }

    // Clase interna para los paneles azules redondeados
    private static class PanelRedondeadoSimple extends JPanel {
        private final int radio;
        private final Color color;
        public PanelRedondeadoSimple(int radio, Color color) {
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