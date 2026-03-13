package com.ucv.view.admin;

import com.ucv.controller.UserController;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SIdeBar2;
import com.ucv.view.components.BotonCerrarSesion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class AnadirEstatus extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color AMARILLO_BOTON = new Color(255, 210, 35);
    private static final Color GRIS_CANCELAR = new Color(150, 150, 150);
    private static final Color GRIS_PLACEHOLDER = new Color(120, 120, 120);

    private final UserController controller;

    public AnadirEstatus(UserController controller) {
        this.controller = controller;

        setTitle("Añadir Estatus · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // --- 1. ENCABEZADO ---
        container.add(new HeaderUCV(), BorderLayout.NORTH);

        // --- 2. SIDEBAR ---
        container.add(new SIdeBar2(this), BorderLayout.WEST);

        // --- 3. PANEL CENTRAL ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);

        JLabel lblTitulo = new JLabel("Ingrese los Datos");
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo, BorderLayout.WEST);

        JButton btnVolver = crearBotonAccion("Volver", GRIS_TARJETA, Color.BLACK);
        btnVolver.setPreferredSize(new Dimension(80, 30));
        btnVolver.addActionListener(e -> controller.volverAAdmin(this));

        JPanel panelBtnVolver = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBtnVolver.setOpaque(false);
        panelBtnVolver.add(btnVolver);
        panelTitulo.add(panelBtnVolver, BorderLayout.EAST);

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelCentral.add(panelTitulo, gbc);

        // Tarjeta Central
        PanelRedondeado tarjeta = new PanelRedondeado(30, GRIS_TARJETA);
        tarjeta.setPreferredSize(new Dimension(550, 260));
        tarjeta.setLayout(null);

        // Input Cédula
        JTextField txtCedula = crearCampoCedula("Cédula");
        txtCedula.setBounds(75, 40, 400, 45);
        tarjeta.add(txtCedula);

        // Dropdown Estatus
        String[] opcionesEstatus = {"Becado", "Exonerado"};
        RoundedComboBox cbEstatus = new RoundedComboBox(opcionesEstatus, "Estatus");
        cbEstatus.setBounds(75, 105, 400, 45);
        cbEstatus.setSelectedIndex(-1);
        tarjeta.add(cbEstatus);

        // Botones Inferiores
        JButton btnCancelar = crearBotonAccion("Cancelar", GRIS_CANCELAR, Color.BLACK);
        btnCancelar.setBounds(235, 180, 110, 35);
        btnCancelar.addActionListener(e -> {
            txtCedula.setText("Cédula");
            txtCedula.setForeground(GRIS_PLACEHOLDER);
            cbEstatus.setSelectedIndex(-1);
        });
        tarjeta.add(btnCancelar);

        JButton btnConfirmar = crearBotonAccion("Confirmar", AMARILLO_BOTON, Color.BLACK);
        btnConfirmar.setBounds(365, 180, 110, 35);

        // --- CAMBIO AQUÍ: Conectamos la vista con el controlador ---
        btnConfirmar.addActionListener(e -> {
            String ced = txtCedula.getText();
            String est = (String) cbEstatus.getSelectedItem();

            // Le pasamos la información al controlador para que él valide y actualice
            controller.actualizarEstatusEstudiante(ced, est, this);
        });

        tarjeta.add(btnConfirmar);

        gbc.gridy = 1;
        panelCentral.add(tarjeta, gbc);

        container.add(panelCentral, BorderLayout.CENTER);

        // --- 4. PANEL DERECHO ---
        container.add(crearContenedorCerrarSesion(), BorderLayout.EAST);

        add(container);
    }

    private JPanel crearContenedorCerrarSesion() {
        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(120, 0));
        BotonCerrarSesion btnCerrar = new BotonCerrarSesion(this);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(25, 0, 0, 0);
        panelDerecho.add(btnCerrar, gbc);
        return panelDerecho;
    }

    // --- MEJORA: Cambio de color dinámico al escribir ---
    private JTextField crearCampoCedula(String placeholder) {
        JTextField campo = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        campo.setOpaque(false);
        campo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        campo.setForeground(GRIS_PLACEHOLDER); // Color gris inicial
        campo.setBorder(new EmptyBorder(0, 15, 0, 15));

        campo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if(campo.getText().equals(placeholder)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK); // Se vuelve negro al escribir
                }
            }
            public void focusLost(FocusEvent e) {
                if(campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    campo.setForeground(GRIS_PLACEHOLDER); // Vuelve a gris si se deja vacío
                }
            }
        });
        return campo;
    }

    private JButton crearBotonAccion(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- MEJORA: Cambio de color forzado en la caja exterior ---
    class RoundedComboBox extends JComboBox<String> {
        public RoundedComboBox(String[] items, String placeholder) {
            super(items);
            setOpaque(false);
            setFocusable(false);
            setBackground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setBorder(new EmptyBorder(0, 15, 0, 15));
            setForeground(GRIS_PLACEHOLDER); // Fuerza el color gris en la caja cerrada

            // Escuchador para cambiar a negro cuando eligen una opción real
            this.addItemListener(e -> {
                if (getSelectedIndex() == -1) {
                    setForeground(GRIS_PLACEHOLDER);
                } else {
                    setForeground(Color.BLACK);
                }
            });

            setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (index == -1 && value == null) {
                        setText(placeholder);
                        c.setForeground(GRIS_PLACEHOLDER);
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                    return c;
                }
            });

            setUI(new BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton btn = new JButton();
                    btn.setContentAreaFilled(false);
                    btn.setBorderPainted(false);
                    btn.setFocusPainted(false);
                    btn.setIcon(new Icon() {
                        public void paintIcon(Component c, Graphics g, int x, int y) {
                            Graphics2D g2 = (Graphics2D) g.create();
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setColor(Color.BLACK);
                            g2.setStroke(new BasicStroke(2f));
                            g2.drawLine(x, y + 2, x + 5, y + 7);
                            g2.drawLine(x + 5, y + 7, x + 10, y + 2);
                            g2.dispose();
                        }
                        public int getIconWidth() { return 12; }
                        public int getIconHeight() { return 12; }
                    });
                    return btn;
                }
            });
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
            g2.dispose();
        }
    }
}