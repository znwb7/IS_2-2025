package com.ucv.view;

import com.ucv.controller.MenuController;
import com.ucv.controller.ccbController;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SIdeBar2;
import com.ucv.view.components.PrimaryButton2;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

    private MenuController controller;

    private JTextField txtPlato, txtBebida, txtPostre;
    private JTextField txtCapacidad, txtCCBResult;
    private JTextField txtCF, txtCV, txtNB, txtMerma;
    private JTextField txtTarifaEst, txtTarifaProf, txtTarifaEmp;
    private JTextField txtPrecioEst, txtPrecioProf, txtPrecioEmp;

    public AgregarPlatoUCV(String fecha, String tipoMenu, MenuController controller) {
        this.controller = controller;
        setTitle("Agregar Platos · Comedor UCV");
        setSize(1920, 800);
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

        // Fecha y TipoMenu: No editables, Sin cursor
        tarjetaPrincipal.add(crearCampoEstiloLogin(fecha, col1X, 50, 260, 45, false, false));
        tarjetaPrincipal.add(crearCampoEstiloLogin(tipoMenu, col1X, 110, 260, 45, false, false));

        // Campos de entrada: Editables, Con cursor
        txtPlato = crearCampoEstiloLogin("Plato fuerte", col1X, 180, 260, 45, true, true);
        txtBebida = crearCampoEstiloLogin("Bebida", col1X, 235, 260, 45, true, true);
        txtPostre = crearCampoEstiloLogin("Postre / Fruta", col1X, 290, 260, 45, true, true);
        txtCapacidad = crearCampoEstiloLogin("Capacidad", col1X, 365, 260, 45, true, true);

        tarjetaPrincipal.add(txtPlato);
        tarjetaPrincipal.add(txtBebida);
        tarjetaPrincipal.add(txtPostre);
        tarjetaPrincipal.add(txtCapacidad);

        // --- COMPONENTES ELIMINADOS DE AQUÍ (lblDisp y campo vacio) ---

        // --- COLUMNA CENTRAL: SECCIÓN CCB ---
        PanelRedondeado panelCCB = new PanelRedondeado(25, Color.WHITE);
        panelCCB.setBounds(375, 50, 240, 360);
        panelCCB.setLayout(null);

        JLabel lblCCBTitulo = new JLabel("CCB", SwingConstants.CENTER);
        lblCCBTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblCCBTitulo.setBounds(0, 15, 240, 30);
        panelCCB.add(lblCCBTitulo);

        String[] labelsCCB = {"CF:", "CV:", "NB:", "Merma:"};
        JTextField[] camposCCB = new JTextField[4];

        for (int i = 0; i < 4; i++) {
            JLabel lbl = new JLabel(labelsCCB[i], SwingConstants.RIGHT);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lbl.setBounds(10, 65 + (i * 55), 70, 30);
            panelCCB.add(lbl);
            camposCCB[i] = crearCampoEstiloLogin("", 90, 65 + (i * 55), 120, 35, true, true);
            panelCCB.add(camposCCB[i]);
        }

        txtCF = camposCCB[0];
        txtCV = camposCCB[1];
        txtNB = camposCCB[2];
        txtMerma = camposCCB[3];

        PrimaryButton2 btnCalcular = new PrimaryButton2("Calcular CCB", AZUL_BOTON_CCB);
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setBounds(30, 290, 180, 45);

        // Lógica de cálculo
        btnCalcular.addActionListener(e -> {
            try {
                String strCF = txtCF.getText().trim().isEmpty() ? "0" : txtCF.getText().replace(",", ".");
                String strCV = txtCV.getText().trim().isEmpty() ? "0" : txtCV.getText().replace(",", ".");
                String strNB = txtNB.getText().trim().isEmpty() ? "0" : txtNB.getText().replace(",", ".");
                String strMerma = txtMerma.getText().trim().isEmpty() ? "0" : txtMerma.getText().replace(",", ".");

                float cf = Float.parseFloat(strCF);
                float cv = Float.parseFloat(strCV);
                float nb = Float.parseFloat(strNB);
                float merma = Float.parseFloat(strMerma);

                float resultado = ccbController.calcularCCB(cf, cv, nb, merma);
                txtCCBResult.setText(String.format(java.util.Locale.US, "%.2f", resultado));

                actualizarPreciosFinales();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Asegúrese de ingresar solo números en los campos del CCB.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                if ("NUM_NEGATIVO".equals(ex.getMessage())) {
                    JOptionPane.showMessageDialog(this, "Los valores para el CCB no pueden ser negativos.", "Valores Inválidos", JOptionPane.ERROR_MESSAGE);
                } else if ("CARACTER_INVALIDO".equals(ex.getMessage())) {
                    JOptionPane.showMessageDialog(this, "Se ha ingresado un caracter inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error en el cálculo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelCCB.add(btnCalcular);
        tarjetaPrincipal.add(panelCCB);

        JLabel lblCCBResult = new JLabel("CCB:");
        lblCCBResult.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCCBResult.setBounds(425, 435, 50, 30);
        tarjetaPrincipal.add(lblCCBResult);

        // CCB Result: No editable, Sin cursor
        txtCCBResult = crearCampoEstiloLogin("0.00", 480, 435, 110, 35, false, false);
        tarjetaPrincipal.add(txtCCBResult);

        JLabel lblNota = new JLabel("*Se requiere el calculo del CCB");
        lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblNota.setBounds(650, 25, 200, 20);
        tarjetaPrincipal.add(lblNota);

        // --- COLUMNA DERECHA: TARIFAS (%) ---
        PanelRedondeado panelTarifas = new PanelRedondeado(25, Color.WHITE);
        panelTarifas.setBounds(630, 50, 280, 220);
        panelTarifas.setLayout(null);

        JLabel lblTarifaT = new JLabel("Tarifas (%)", SwingConstants.CENTER);
        lblTarifaT.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTarifaT.setBounds(0, 15, 280, 30);
        panelTarifas.add(lblTarifaT);

        JLabel lblEst = new JLabel("Estudiante");
        lblEst.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblEst.setBounds(20, 60, 100, 30);
        txtTarifaEst = crearCampoEstiloLogin("20-30", 140, 60, 110, 35, true, true);
        panelTarifas.add(lblEst);
        panelTarifas.add(txtTarifaEst);

        JLabel lblProf = new JLabel("Profesor");
        lblProf.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblProf.setBounds(20, 110, 100, 30);
        txtTarifaProf = crearCampoEstiloLogin("70-90", 140, 110, 110, 35, true, true);
        panelTarifas.add(lblProf);
        panelTarifas.add(txtTarifaProf);

        JLabel lblEmp = new JLabel("Empleado");
        lblEmp.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblEmp.setBounds(20, 160, 100, 30);
        txtTarifaEmp = crearCampoEstiloLogin("90-110", 140, 160, 110, 35, true, true);
        panelTarifas.add(lblEmp);
        panelTarifas.add(txtTarifaEmp);

        tarjetaPrincipal.add(panelTarifas);

        JLabel lblFinal = new JLabel("Precio final para el comensal");
        lblFinal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFinal.setBounds(640, 285, 250, 25);
        tarjetaPrincipal.add(lblFinal);

        // Precios finales: No editables, Sin cursor
        txtPrecioEst = crearCampoEstiloLogin("0.00", 740, 320, 110, 35, false, false);
        txtPrecioProf = crearCampoEstiloLogin("0.00", 740, 365, 110, 35, false, false);
        txtPrecioEmp = crearCampoEstiloLogin("0.00", 740, 410, 110, 35, false, false);

        String[] tipos = {"Estudiante:", "Profesor:", "Empleado:"};
        JTextField[] camposPrecios = {txtPrecioEst, txtPrecioProf, txtPrecioEmp};

        for (int i = 0; i < 3; i++) {
            JLabel lbl = new JLabel(tipos[i], SwingConstants.RIGHT);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setBounds(630, 320 + (i * 45), 100, 30);
            tarjetaPrincipal.add(lbl);

            tarjetaPrincipal.add(camposPrecios[i]);

            JLabel bs = new JLabel("Bs.");
            bs.setBounds(860, 320 + (i * 45), 40, 30);
            tarjetaPrincipal.add(bs);
        }

        // --- LÓGICA DINÁMICA: ESCUCHAR LOS CAMBIOS EN LAS TARIFAS ---
        DocumentListener calculoDinamico = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarPreciosFinales(); }
            public void removeUpdate(DocumentEvent e) { actualizarPreciosFinales(); }
            public void changedUpdate(DocumentEvent e) { actualizarPreciosFinales(); }
        };
        txtTarifaEst.getDocument().addDocumentListener(calculoDinamico);
        txtTarifaProf.getDocument().addDocumentListener(calculoDinamico);
        txtTarifaEmp.getDocument().addDocumentListener(calculoDinamico);

        // --- BOTONES DE ACCIÓN ---
        PrimaryButton2 btnCanc = new PrimaryButton2("Cancelar", GRIS_CANCELAR);
        btnCanc.setBounds(635, 465, 120, 40);
        btnCanc.addActionListener(e -> {
            controller.procesarFechaSeleccionada(fecha, this);
        });
        tarjetaPrincipal.add(btnCanc);

        PrimaryButton2 btnAgr = new PrimaryButton2("Agregar Menu", AMARILLO_BOTON);
        btnAgr.setBounds(765, 465, 170, 40);
        btnAgr.addActionListener(e -> {
            String pFuerte = txtPlato.getText().equals("Plato fuerte") ? "N/A" : txtPlato.getText();
            String pBebida = txtBebida.getText().equals("Bebida") ? "N/A" : txtBebida.getText();
            String pPostre = txtPostre.getText().equals("Postre / Fruta") ? "N/A" : txtPostre.getText();

            String pEst = txtPrecioEst.getText().equals("0.00") || txtPrecioEst.getText().isEmpty() ? "0" : txtPrecioEst.getText();
            String pProf = txtPrecioProf.getText().equals("0.00") || txtPrecioProf.getText().isEmpty() ? "0" : txtPrecioProf.getText();
            String pEmp = txtPrecioEmp.getText().equals("0.00") || txtPrecioEmp.getText().isEmpty() ? "0" : txtPrecioEmp.getText();

            String cap = txtCapacidad.getText().equals("Capacidad") || txtCapacidad.getText().isEmpty() ? "500" : txtCapacidad.getText();
            String ccb = txtCCBResult.getText().isEmpty() ? "0.00" : txtCCBResult.getText();

            controller.registrarNuevoPlato(fecha, tipoMenu, pFuerte, pBebida, pPostre, pEst, pProf, pEmp, cap, ccb, this);
        });
        tarjetaPrincipal.add(btnAgr);

        panelCentral.add(tarjetaPrincipal, gbcCentrar);
        container.add(panelCentral, BorderLayout.CENTER);
        container.add(crearFooter(), BorderLayout.SOUTH);
        add(container);
    }

    private void actualizarPreciosFinales() {
        try {
            if (txtCCBResult.getText().equals("0.00") || txtCCBResult.getText().isEmpty()) {
                return;
            }
            float ccb = Float.parseFloat(txtCCBResult.getText().replace(",", "."));

            float tEst = obtenerTarifa(txtTarifaEst.getText(), "20-30");
            float tProf = obtenerTarifa(txtTarifaProf.getText(), "70-90");
            float tEmp = obtenerTarifa(txtTarifaEmp.getText(), "90-110");

            txtPrecioEst.setText(String.format(java.util.Locale.US, "%.2f", ccb * (tEst / 100f)));
            txtPrecioProf.setText(String.format(java.util.Locale.US, "%.2f", ccb * (tProf / 100f)));
            txtPrecioEmp.setText(String.format(java.util.Locale.US, "%.2f", ccb * (tEmp / 100f)));
        } catch (Exception e) {
            // Se ignoran errores si el usuario está tipeando y la cadena está incompleta
        }
    }

    private float obtenerTarifa(String texto, String placeholder) {
        if (texto.equals(placeholder) || texto.trim().isEmpty()) {
            return 0f;
        }
        try {
            return Float.parseFloat(texto.replace(",", "."));
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    // --- NUEVA FIRMA DEL COMPONENTE DE TEXTO CON FOCUSABLE ---
    private JTextField crearCampoEstiloLogin(String placeholder, int x, int y, int width, int height, boolean editable, boolean permitirCursor) {
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

        // --- LÓGICA DE FOCO Y EDICIÓN ---
        campo.setEditable(editable);
        campo.setFocusable(permitirCursor);

        campo.setHorizontalAlignment(width < 150 ? JTextField.CENTER : JTextField.LEFT);

        // Solo aplicamos el FocusListener si el campo permite el cursor y es editable
        if (permitirCursor && editable && !placeholder.isEmpty()) {
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
                        actualizarPreciosFinales();
                    }
                }
            });
        }

        if (!editable) {
            campo.setForeground(new Color(80, 80, 80));
        }

        return campo;
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
}