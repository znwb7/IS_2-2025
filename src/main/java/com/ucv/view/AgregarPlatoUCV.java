package com.ucv.view;

import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SIdeBar2;
import com.ucv.view.components.PrimaryButton2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class AgregarPlatoUCV extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color AZUL_BOTON_CCB = new Color(28, 77, 149);
    private static final Color AMARILLO_BOTON = new Color(255, 210, 35);
    private static final Color GRIS_CANCELAR = new Color(190, 190, 190);
    private static final Color GRIS_INPUT = new Color(235, 235, 235);

    public AgregarPlatoUCV(String fecha, String tipoMenu) {
        setTitle("Agregar Platos · Comedor UCV");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SIdeBar2(this), BorderLayout.WEST);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);

        GridBagConstraints gbcCentrar = new GridBagConstraints();
        gbcCentrar.gridx = 0;
        gbcCentrar.gridy = 0;
        gbcCentrar.anchor = GridBagConstraints.CENTER;

        PanelRedondeado tarjetaPrincipal = new PanelRedondeado(40, GRIS_TARJETA);
        tarjetaPrincipal.setPreferredSize(new Dimension(950, 520));
        tarjetaPrincipal.setMinimumSize(new Dimension(950, 520));
        tarjetaPrincipal.setLayout(null);

        // --- COLUMNA IZQUIERDA: DATOS DEL PLATO ---
        int col1X = 60;
        tarjetaPrincipal.add(crearCampoEstiloLogin(fecha, col1X, 50, 260, 45, false));
        tarjetaPrincipal.add(crearCampoEstiloLogin(tipoMenu, col1X, 110, 260, 45, false));
        tarjetaPrincipal.add(crearCampoEstiloLogin("Plato fuerte", col1X, 180, 260, 45, true));
        tarjetaPrincipal.add(crearCampoEstiloLogin("Bebida", col1X, 235, 260, 45, true));
        tarjetaPrincipal.add(crearCampoEstiloLogin("Postre / Fruta", col1X, 290, 260, 45, true));
        tarjetaPrincipal.add(crearCampoEstiloLogin("Capacidad", col1X, 365, 260, 45, true));

        JLabel lblDisp = new JLabel("Disponibles:");
        lblDisp.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDisp.setBounds(col1X + 20, 435, 120, 30);
        tarjetaPrincipal.add(lblDisp);
        tarjetaPrincipal.add(crearCampoEstiloLogin("", col1X + 150, 435, 110, 35, true));

        // --- COLUMNA CENTRAL: SECCIÓN CCB ---
        PanelRedondeado panelCCB = new PanelRedondeado(25, Color.WHITE);
        panelCCB.setBounds(375, 50, 240, 360);
        panelCCB.setLayout(null);

        JLabel lblCCBTitulo = new JLabel("CCB", SwingConstants.CENTER);
        lblCCBTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblCCBTitulo.setBounds(0, 15, 240, 30);
        panelCCB.add(lblCCBTitulo);

        String[] labelsCCB = {"CF:", "CV:", "NB:", "Merma:"};
        for (int i = 0; i < 4; i++) {
            JLabel lbl = new JLabel(labelsCCB[i], SwingConstants.RIGHT);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lbl.setBounds(10, 65 + (i * 55), 70, 30);
            panelCCB.add(lbl);
            panelCCB.add(crearCampoEstiloLogin("", 90, 65 + (i * 55), 120, 35, true));
        }

        PrimaryButton2 btnCalcular = new PrimaryButton2("Calcular CCB", AZUL_BOTON_CCB);
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setBounds(30, 290, 180, 45);
        panelCCB.add(btnCalcular);
        tarjetaPrincipal.add(panelCCB);

        JLabel lblCCBResult = new JLabel("CCB:");
        lblCCBResult.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCCBResult.setBounds(425, 435, 50, 30);
        tarjetaPrincipal.add(lblCCBResult);
        tarjetaPrincipal.add(crearCampoEstiloLogin("", 480, 435, 110, 35, true));

        // --- COLUMNA DERECHA: TARIFAS (%) ---
        JLabel lblNota = new JLabel("*Se requiere el calculo del CCB");
        lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblNota.setBounds(650, 25, 200, 20);
        tarjetaPrincipal.add(lblNota);

        PanelRedondeado panelTarifas = new PanelRedondeado(25, Color.WHITE);
        panelTarifas.setBounds(630, 50, 280, 220);
        panelTarifas.setLayout(null);

        JLabel lblTarifaT = new JLabel("Tarifas (%)", SwingConstants.CENTER);
        lblTarifaT.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTarifaT.setBounds(0, 15, 280, 30);
        panelTarifas.add(lblTarifaT);

        String[][] filasTarifas = {{"Estudiante", "20-30"}, {"Profesor", "70-90"}, {"Empleado", "90-110"}};
        for (int i = 0; i < 3; i++) {
            JLabel lbl = new JLabel(filasTarifas[i][0]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lbl.setBounds(20, 60 + (i * 50), 100, 30);
            panelTarifas.add(lbl);
            panelTarifas.add(crearCampoEstiloLogin(filasTarifas[i][1], 140, 60 + (i * 50), 110, 35, true));
        }
        tarjetaPrincipal.add(panelTarifas);

        JLabel lblFinal = new JLabel("Precio final para el comensal");
        lblFinal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFinal.setBounds(640, 285, 250, 25);
        tarjetaPrincipal.add(lblFinal);

        String[] tipos = {"Estudiante:", "Profesor:", "Empleado:"};
        for (int i = 0; i < 3; i++) {
            JLabel lbl = new JLabel(tipos[i], SwingConstants.RIGHT);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setBounds(630, 320 + (i * 45), 100, 30);
            tarjetaPrincipal.add(lbl);
            tarjetaPrincipal.add(crearCampoEstiloLogin("", 740, 320 + (i * 45), 110, 35, true));
            JLabel bs = new JLabel("Bs.");
            bs.setBounds(860, 320 + (i * 45), 40, 30);
            tarjetaPrincipal.add(bs);
        }

        // --- BOTÓN CANCELAR: REGRESA A LA VISTA ANTERIOR SIN CAMBIOS ---
        PrimaryButton2 btnCanc = new PrimaryButton2("Cancelar", GRIS_CANCELAR);
        btnCanc.setBounds(635, 465, 120, 40);
        btnCanc.addActionListener(e -> {
            dispose();
            new FechaMenusNewUCV(fecha, false, false).setVisible(true);
        });
        tarjetaPrincipal.add(btnCanc);

        // --- BOTÓN AGREGAR: GUARDA Y REGRESA MOSTRANDO LOS DATOS ---
        PrimaryButton2 btnAgr = new PrimaryButton2("Agregar Menu", AMARILLO_BOTON);
        btnAgr.setBounds(765, 465, 170, 40);
        btnAgr.addActionListener(e -> {
            dispose();
            boolean esDesayuno = tipoMenu.equalsIgnoreCase("Desayuno");
            boolean esAlmuerzo = tipoMenu.equalsIgnoreCase("Almuerzo");
            new FechaMenusNewUCV(fecha, esDesayuno, esAlmuerzo).setVisible(true);
        });
        tarjetaPrincipal.add(btnAgr);

        panelCentral.add(tarjetaPrincipal, gbcCentrar);
        container.add(panelCentral, BorderLayout.CENTER);

        container.add(crearFooter(), BorderLayout.SOUTH);
        add(container);
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
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                LoginUCV login = new LoginUCV();
                login.setController(new com.ucv.controller.UserController());
                login.setVisible(true);
            }
        });

        footer.add(cerrarSesion);
        return footer;
    }

    private JTextField crearCampoEstiloLogin(String placeholder, int x, int y, int width, int height, boolean editable) {
        JTextField campo = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        campo.setOpaque(false);
        campo.setBounds(x, y, width, height);
        campo.setBackground(GRIS_INPUT);
        campo.setFont(new Font("Segoe UI", editable ? Font.PLAIN : Font.BOLD, 14));
        campo.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        campo.setEditable(editable);
        campo.setHorizontalAlignment(width < 150 ? JTextField.CENTER : JTextField.LEFT);

        if (editable && !placeholder.isEmpty()) {
            campo.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (campo.getText().equals(placeholder)) {
                        campo.setText("");
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (campo.getText().isEmpty()) {
                        campo.setText(placeholder);
                    }
                }
            });
        }

        if (!editable) {
            campo.setForeground(new Color(80, 80, 80));
        }

        return campo;
    }

    static class PanelRedondeado extends JPanel {
        private final int radio;
        private final Color color;
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
        SwingUtilities.invokeLater(() -> {
            new AgregarPlatoUCV("24/02/2026", "Almuerzo").setVisible(true);
        });
    }
}