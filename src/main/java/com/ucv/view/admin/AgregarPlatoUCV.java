package com.ucv.view.admin;

import com.ucv.controller.MenuController;
import com.ucv.controller.ccbController;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SIdeBar2;
import com.ucv.view.components.PrimaryButton2;
import com.ucv.view.components.BotonCerrarSesion;

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

    private JTextField txtPlato, txtBebida, txtPostre, txtCapacidad, txtCCBResult;
    private JTextField txtCF, txtCV, txtNB, txtMerma;
    private JTextField txtTarifaEst, txtTarifaProf, txtTarifaEmp, txtTarifaBec;
    private JTextField txtPrecioEst, txtPrecioProf, txtPrecioEmp, txtPrecioBec;

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
        tarjetaPrincipal.setPreferredSize(new Dimension(950, 580));       
        tarjetaPrincipal.setLayout(null);

        int col1X = 60;

        tarjetaPrincipal.add(crearCampoEstiloLogin(fecha, col1X, 50, 260, 45, false, false));
        tarjetaPrincipal.add(crearCampoEstiloLogin(tipoMenu, col1X, 110, 260, 45, false, false));

        txtPlato = crearCampoEstiloLogin("Plato fuerte", col1X, 180, 260, 45, true, true);
        txtBebida = crearCampoEstiloLogin("Bebida", col1X, 235, 260, 45, true, true);
        txtPostre = crearCampoEstiloLogin("Postre / Fruta", col1X, 290, 260, 45, true, true);
        txtCapacidad = crearCampoEstiloLogin("Capacidad", col1X, 365, 260, 45, true, true);

        tarjetaPrincipal.add(txtPlato);
        tarjetaPrincipal.add(txtBebida);
        tarjetaPrincipal.add(txtPostre);
        tarjetaPrincipal.add(txtCapacidad);

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
        btnCalcular.addActionListener(e -> calcularCCBLogic());
        panelCCB.add(btnCalcular);

        tarjetaPrincipal.add(panelCCB);

        JLabel lblCCB = new JLabel("CCB:");
        lblCCB.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblCCB.setBounds(425, 435, 50, 30);
        tarjetaPrincipal.add(lblCCB);

        txtCCBResult = crearCampoEstiloLogin("0.00", 480, 435, 110, 35, false, false);
        tarjetaPrincipal.add(txtCCBResult);

        JLabel lblNota = new JLabel("*Se requiere el calculo del CCB");
        lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblNota.setBounds(650, 25, 200, 20);
        tarjetaPrincipal.add(lblNota);

        PanelRedondeado panelTarifas = new PanelRedondeado(25, Color.WHITE);
        panelTarifas.setBounds(630, 50, 280, 260);
        panelTarifas.setLayout(null);

        JLabel lblTarifaT = new JLabel("Tarifas (%)", SwingConstants.CENTER);
        lblTarifaT.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTarifaT.setBounds(0, 15, 280, 30);
        panelTarifas.add(lblTarifaT);

        JLabel lblEst = new JLabel("Estudiante");
        lblEst.setBounds(20, 60, 100, 30);
        panelTarifas.add(lblEst);

        JLabel lblProf = new JLabel("Profesor");
        lblProf.setBounds(20, 110, 100, 30);
        panelTarifas.add(lblProf);

        JLabel lblEmp = new JLabel("Empleado");
        lblEmp.setBounds(20, 160, 100, 30);
        panelTarifas.add(lblEmp);

        JLabel lblBec = new JLabel("Becado");
        lblBec.setBounds(20, 210, 100, 30);
        panelTarifas.add(lblBec);

        txtTarifaEst = crearCampoEstiloLogin("20-30", 140, 60, 110, 35, true, true);
        txtTarifaProf = crearCampoEstiloLogin("70-90", 140, 110, 110, 35, true, true);
        txtTarifaEmp = crearCampoEstiloLogin("90-110", 140, 160, 110, 35, true, true);
        txtTarifaBec = crearCampoEstiloLogin("5-19", 140, 210, 110, 35, true, true);

        panelTarifas.add(txtTarifaEst);
        panelTarifas.add(txtTarifaProf);
        panelTarifas.add(txtTarifaEmp);
        panelTarifas.add(txtTarifaBec);

        tarjetaPrincipal.add(panelTarifas);

        JLabel lblFinal = new JLabel("Precio final para el comensal");
        lblFinal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFinal.setBounds(640, 285, 250, 25);
        tarjetaPrincipal.add(lblFinal);

        txtPrecioEst = crearCampoEstiloLogin("0.00", 740, 320, 110, 35, false, false);
        txtPrecioProf = crearCampoEstiloLogin("0.00", 740, 365, 110, 35, false, false);
        txtPrecioEmp = crearCampoEstiloLogin("0.00", 740, 410, 110, 35, false, false);
        txtPrecioBec = crearCampoEstiloLogin("0.00", 740, 455, 110, 35, false, false);

        JTextField[] camposPrecios = {txtPrecioEst, txtPrecioProf, txtPrecioEmp, txtPrecioBec};
        String[] tipos = {"Estudiante:", "Profesor:", "Empleado:", "Becado:"};

        for (int i = 0; i < 4; i++) {

            JLabel lbl = new JLabel(tipos[i], SwingConstants.RIGHT);
            lbl.setBounds(630, 320 + (i * 45), 100, 30);
            tarjetaPrincipal.add(lbl);

            tarjetaPrincipal.add(camposPrecios[i]);

            JLabel bs = new JLabel("Bs.");
            bs.setBounds(860, 320 + (i * 45), 40, 30);
            tarjetaPrincipal.add(bs);
        }

        PrimaryButton2 btnCanc = new PrimaryButton2("Cancelar", GRIS_CANCELAR);
        btnCanc.setBounds(635, 515, 120, 40);
        btnCanc.addActionListener(e -> controller.procesarFechaSeleccionada(fecha, this));
        tarjetaPrincipal.add(btnCanc);

        PrimaryButton2 btnAgr = new PrimaryButton2("Agregar Menu", AMARILLO_BOTON);
        btnAgr.setBounds(765, 515, 170, 40);
        btnAgr.addActionListener(e -> registrarPlatoAction(fecha, tipoMenu));
        tarjetaPrincipal.add(btnAgr);

        panelCentral.add(tarjetaPrincipal, gbcCentrar);
        container.add(panelCentral, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new GridBagLayout());
        panelDerecho.setOpaque(false);
        panelDerecho.setPreferredSize(new Dimension(120, 0));

        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.anchor = GridBagConstraints.NORTH;
        gbcBtn.weighty = 1.0;
        gbcBtn.insets = new Insets(25, 0, 0, 0);

        panelDerecho.add(new BotonCerrarSesion(this), gbcBtn);

        container.add(panelDerecho, BorderLayout.EAST);

        add(container);

        configurarEscuchadores();
    }

    private void calcularCCBLogic() {
        try {

            float cf = Float.parseFloat(txtCF.getText().replace(",", "."));
            float cv = Float.parseFloat(txtCV.getText().replace(",", "."));
            float nb = Float.parseFloat(txtNB.getText().replace(",", "."));
            float merma = Float.parseFloat(txtMerma.getText().replace(",", "."));

            float resultado = ccbController.calcularCCB(cf, cv, nb, merma);

            txtCCBResult.setText(String.format(java.util.Locale.US, "%.2f", resultado));

            actualizarPreciosFinales();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en los datos del CCB", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

private void registrarPlatoAction(String fecha, String tipoMenu) {

    String pFuerte = txtPlato.getText().equals("Plato fuerte") ? "N/A" : txtPlato.getText();
    String pBebida = txtBebida.getText().equals("Bebida") ? "N/A" : txtBebida.getText();
    String pPostre = txtPostre.getText().equals("Postre / Fruta") ? "N/A" : txtPostre.getText();

    String pEst = txtPrecioEst.getText().equals("0.00") || txtPrecioEst.getText().isEmpty() ? "0" : txtPrecioEst.getText();
    String pProf = txtPrecioProf.getText().equals("0.00") || txtPrecioProf.getText().isEmpty() ? "0" : txtPrecioProf.getText();
    String pEmp = txtPrecioEmp.getText().equals("0.00") || txtPrecioEmp.getText().isEmpty() ? "0" : txtPrecioEmp.getText();
    String pBec = txtPrecioBec.getText().equals("0.00") || txtPrecioBec.getText().isEmpty() ? "0" : txtPrecioBec.getText();

    String cap = txtCapacidad.getText().equals("Capacidad") || txtCapacidad.getText().isEmpty() ? "500" : txtCapacidad.getText();
    String ccb = txtCCBResult.getText().isEmpty() ? "0.00" : txtCCBResult.getText();

    String tEst = txtTarifaEst.getText().equals("20-30") ? "0" : txtTarifaEst.getText();
    String tProf = txtTarifaProf.getText().equals("70-90") ? "0" : txtTarifaProf.getText();
    String tEmp = txtTarifaEmp.getText().equals("90-110") ? "0" : txtTarifaEmp.getText();
    String tBec = txtTarifaBec.getText().equals("5-19") ? "0" : txtTarifaBec.getText();

    controller.registrarNuevoPlato(
            fecha, tipoMenu,
            pFuerte, pBebida, pPostre,
            pEst, pProf, pEmp, pBec,
            cap, ccb,
            tEst, tProf, tEmp, tBec,
            this
    );
}

    private void configurarEscuchadores() {

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizarPreciosFinales(); }
            public void removeUpdate(DocumentEvent e) { actualizarPreciosFinales(); }
            public void changedUpdate(DocumentEvent e) { actualizarPreciosFinales(); }
        };

        txtTarifaEst.getDocument().addDocumentListener(dl);
        txtTarifaProf.getDocument().addDocumentListener(dl);
        txtTarifaEmp.getDocument().addDocumentListener(dl);
        txtTarifaBec.getDocument().addDocumentListener(dl);
    }

    private void actualizarPreciosFinales() {

        try {

            float ccb = Float.parseFloat(txtCCBResult.getText().replace(",", "."));

            txtPrecioEst.setText(String.format(java.util.Locale.US, "%.2f", ccb * (obtenerTarifa(txtTarifaEst.getText(), "20-30") / 100f)));
            txtPrecioProf.setText(String.format(java.util.Locale.US, "%.2f", ccb * (obtenerTarifa(txtTarifaProf.getText(), "70-90") / 100f)));
            txtPrecioEmp.setText(String.format(java.util.Locale.US, "%.2f", ccb * (obtenerTarifa(txtTarifaEmp.getText(), "90-110") / 100f)));
            txtPrecioBec.setText(String.format(java.util.Locale.US, "%.2f", ccb * (obtenerTarifa(txtTarifaBec.getText(), "5-19") / 100f)));

        } catch (Exception ignored) {}
    }

    private float obtenerTarifa(String texto, String placeholder) {

        if (texto.equals(placeholder) || texto.trim().isEmpty()) return 0f;

        try {
            return Float.parseFloat(texto.replace(",", "."));
        } catch (Exception e) {
            return 0f;
        }
    }

    private JTextField crearCampoEstiloLogin(String placeholder, int x, int y, int width, int height, boolean editable, boolean permitirCursor) {

        JTextField campo = new JTextField(placeholder) {

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
        campo.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        campo.setEditable(editable);
        campo.setFocusable(permitirCursor);

        if (permitirCursor && editable) {

            campo.addFocusListener(new FocusAdapter() {

                public void focusGained(FocusEvent e) {
                    if (campo.getText().equals(placeholder)) campo.setText("");
                }

                public void focusLost(FocusEvent e) {
                    if (campo.getText().isEmpty()) campo.setText(placeholder);
                }
            });
        }

        return campo;
    }

    static class PanelRedondeado extends JPanel {

        private final int radio;
        private final Color color;

        public PanelRedondeado(int radio, Color color) {

            this.radio = radio;
            this.color = color;

            setOpaque(false);
        }

        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(color);

            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radio, radio));

            g2.dispose();
        }
    }
}