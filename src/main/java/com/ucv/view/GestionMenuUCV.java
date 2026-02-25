package com.ucv.view;

import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SIdeBar2;
import com.ucv.view.components.PrimaryButton2;
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

    public GestionMenuUCV() {
        setTitle("Gestión de Menús · Comedor UCV");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Contenedor Principal
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- 1. ENCABEZADO ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR (Casita configurada como Admin) ---
        container.add(new SIdeBar2(this), BorderLayout.WEST);

        // --- 3. CUERPO CENTRAL ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);

        // Tarjeta Gris
        PanelRedondeado tarjeta = new PanelRedondeado(40, GRIS_TARJETA);
        tarjeta.setPreferredSize(new Dimension(750, 240));
        tarjeta.setLayout(null);

        // Texto Instrucción
        JLabel lblInstruccion = new JLabel("Seleccione la fecha de los menus que quiere modificar", SwingConstants.CENTER);
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 24));
        lblInstruccion.setBounds(0, 25, 750, 40);
        tarjeta.add(lblInstruccion);

        // --- COMBOBOXES ---
        String[] dias = {"Dia", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
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

        // --- BOTONES ---
        PrimaryButton2 btnCancelar = new PrimaryButton2("Cancelar", GRIS_CANCELAR);
        btnCancelar.setBounds(50, 170, 200, 55);
        btnCancelar.addActionListener(e -> {
            dispose();
            new AdminUCV("Administrador").setVisible(true);
        });

        PrimaryButton2 btnConfirmar = new PrimaryButton2("Confirmar", AMARILLO_BOTON);
        btnConfirmar.setBounds(500, 170, 200, 55);

        // REDIRECCIÓN A DETALLE MENÚS
        btnConfirmar.addActionListener(e -> {
            String d = (String) cbDia.getSelectedItem();
            String m = (String) cbMes.getSelectedItem();
            String a = (String) cbAno.getSelectedItem();

            if (d.equals("Dia") || m.equals("Mes") || a.equals("Año")) {
                JOptionPane.showMessageDialog(this, "Por favor seleccione una fecha válida", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String fechaFormateada = d + "/" + m + "/" + a;
                dispose();
                new FechaMenusNewUCV(fechaFormateada,false,false).setVisible(true);
            }
        });

        tarjeta.add(btnCancelar);
        tarjeta.add(btnConfirmar);

        panelCentral.add(tarjeta);
        container.add(panelCentral, BorderLayout.CENTER);

        // --- 4. FOOTER ---
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 50));

        JLabel lblCerrarSesion = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lblCerrarSesion.setForeground(Color.WHITE);
        lblCerrarSesion.setFont(new Font("Arial", Font.PLAIN, 22));
        lblCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                dispose();
                com.ucv.ComedorApp.main(null);
            }
        });

        footer.add(lblCerrarSesion);
        container.add(footer, BorderLayout.SOUTH);

        add(container);
    }

    // --- CLASES INTERNAS ---
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
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radio, radio));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GestionMenuUCV().setVisible(true));
    }
}