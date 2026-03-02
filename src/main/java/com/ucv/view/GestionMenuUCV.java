package com.ucv.view;

import com.ucv.controller.MenuController;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SIdeBar2;
import com.ucv.view.components.PrimaryButton2;
import com.ucv.view.components.BotonCerrarSesion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GestionMenuUCV extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color AMARILLO_BOTON = new Color(255, 210, 35);
    private static final Color GRIS_CANCELAR = new Color(190, 190, 190);
    private static final Color GRIS_INPUT = new Color(235, 235, 235);

    private final MenuController controller;

    public GestionMenuUCV() {
        this(new MenuController());
    }

    public GestionMenuUCV(MenuController controller) {
        this.controller = controller;
        setTitle("Gestión de Menús · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Contenedor principal con fondo azul
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- 1. ENCABEZADO ESTÁNDAR ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR IZQUIERDO ---
        container.add(new SIdeBar2(this), BorderLayout.WEST);

        // --- 3. PANEL CENTRAL (Selección de Fecha) ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);

        PanelRedondeado tarjeta = new PanelRedondeado(40, GRIS_TARJETA);
        tarjeta.setPreferredSize(new Dimension(750, 240));
        tarjeta.setLayout(null);

        JLabel lblInstruccion = new JLabel("Seleccione la fecha de los menús que quiere modificar", SwingConstants.CENTER);
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 24));
        lblInstruccion.setBounds(0, 25, 750, 40);
        tarjeta.add(lblInstruccion);

        String[] dias = {"Día", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        String[] meses = {"Mes", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] anos = {"Año", "2025", "2026", "2027", "2028", "2029", "2030"};

        RoundedComboBox cbDia = new RoundedComboBox(dias);
        cbDia.setBounds(50, 90, 200, 55);

        RoundedComboBox cbMes = new RoundedComboBox(meses);
        cbMes.setBounds(275, 90, 200, 55);

        RoundedComboBox cbAno = new RoundedComboBox(anos);
        cbAno.setBounds(500, 90, 200, 55);

        tarjeta.add(cbDia);
        tarjeta.add(cbMes);
        tarjeta.add(cbAno);

        PrimaryButton2 btnCancelar = new PrimaryButton2("Cancelar", GRIS_CANCELAR);
        btnCancelar.setBounds(50, 170, 200, 55);
        btnCancelar.addActionListener(e -> controller.volverAAdmin(this));

        PrimaryButton2 btnConfirmar = new PrimaryButton2("Confirmar", AMARILLO_BOTON);
        btnConfirmar.setBounds(500, 170, 200, 55);
        btnConfirmar.addActionListener(e -> {
            String d = (String) cbDia.getSelectedItem();
            String m = (String) cbMes.getSelectedItem();
            String a = (String) cbAno.getSelectedItem();

            if (d.equals("Día") || m.equals("Mes") || a.equals("Año")) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione una fecha válida", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String fechaFormateada = d + "/" + m + "/" + a;
                controller.procesarFechaSeleccionada(fechaFormateada, this);
            }
        });

        tarjeta.add(btnCancelar);
        tarjeta.add(btnConfirmar);

        panelCentral.add(tarjeta);
        container.add(panelCentral, BorderLayout.CENTER);

        // --- 4. PANEL DERECHO (Botón Cerrar Sesión) ---
        container.add(crearContenedorCerrarSesion(), BorderLayout.EAST);

        add(container);
    }

    private JPanel crearContenedorCerrarSesion() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(120, 0));

        // Componente con icono y lógica de logout
        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        // Insets estandarizados para que el botón no se mueva de lugar entre ventanas
        gbc.insets = new Insets(25, 0, 0, 0);

        panelDerecho.add(btnCerrar, gbc);
        return panelDerecho;
    }

    // --- COMPONENTES INTERNOS ---

    class RoundedComboBox extends JComboBox<String> {
        public RoundedComboBox(String[] items) {
            super(items);
            setOpaque(false);
            setFocusable(false);
            setBackground(GRIS_INPUT);
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setBorder(new EmptyBorder(0, 15, 0, 15));

            setUI(new BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton btn = new JButton();
                    btn.setContentAreaFilled(false);
                    btn.setBorderPainted(false);
                    btn.setFocusPainted(false);
                    btn.setIcon(new Icon() {
                        @Override
                        public void paintIcon(Component c, Graphics g, int x, int y) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(Color.BLACK);
                            g2.setStroke(new BasicStroke(2f));
                            g2.drawLine(x, y + 2, x + 5, y + 7);
                            g2.drawLine(x + 5, y + 7, x + 10, y + 2);
                            g2.dispose();
                        }
                        @Override public int getIconWidth() { return 12; }
                        @Override public int getIconHeight() { return 12; }
                    });
                    return btn;
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            g2.dispose();
            super.paintComponent(g);
        }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionMenuUCV().setVisible(true));
    }
}