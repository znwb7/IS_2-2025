package com.ucv.view;

import com.ucv.controller.PagoController;
import com.ucv.model.PagoModel;
import com.ucv.model.DataBase;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import com.ucv.view.components.PrimaryButton2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class PagoMovilUCV extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color GRIS_TARJETA = new Color(225, 225, 225);
    private static final Color AMARILLO_BOTON = new Color(255, 210, 35);
    private static final Color GRIS_CANCELAR = new Color(190, 190, 190);
    private static final Color AZUL_TEXTO = new Color(18, 71, 150);

    private final PagoController controlador;

    private JTextField txtCedula, txtMonto, txtReferencia;
    private final String usuarioID;
    private final BilleteraUCV parentFrame;

    public BilleteraUCV getParentFrame() {
    return parentFrame;
    }

    public PagoMovilUCV(String usuarioID, BilleteraUCV parentFrame) {
        this.usuarioID = usuarioID;
        System.out.println("ID del usuario en PagoMovilUCV: " + usuarioID); // Verificación de ID
        this.parentFrame = parentFrame;

        // Modelo y controlador
        PagoModel modelo = new PagoModel();
        this.controlador = new PagoController(modelo, usuarioID);

        setTitle("Pago Móvil · Comedor UCV");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);
        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SideBar(this, usuarioID), BorderLayout.WEST);

        // Panel central
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);

        PanelRedondeado tarjeta = new PanelRedondeado(40, GRIS_TARJETA);
        tarjeta.setPreferredSize(new Dimension(700, 500));
        tarjeta.setLayout(null);

        // Datos de pago
        JLabel lblTituloPago = new JLabel("Realiza tu pago móvil a:");
        lblTituloPago.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloPago.setForeground(AZUL_TEXTO);
        lblTituloPago.setBounds(50, 30, 400, 40);
        tarjeta.add(lblTituloPago);

        String[][] datos = {
                {"Banco:", "MERCANTIL"},
                {"RIF:", "J-12345678"},
                {"Teléfono:", "0412-1234567"}
        };

        int yBase = 80;
        for (int i = 0; i < datos.length; i++) {
            JLabel lblKey = new JLabel(datos[i][0]);
            lblKey.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            lblKey.setBounds(50, yBase + (i * 30), 200, 25);
            tarjeta.add(lblKey);

            JLabel lblValue = new JLabel(datos[i][1]);
            lblValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblValue.setBounds(250, yBase + (i * 30), 320, 25);
            lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
            tarjeta.add(lblValue);
        }

        // Formulario
        JLabel lblInstruccion = new JLabel("Ingresa los datos de tu Pago Móvil");
        lblInstruccion.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblInstruccion.setForeground(AZUL_TEXTO);
        lblInstruccion.setBounds(50, 200, 400, 30);
        tarjeta.add(lblInstruccion);

        int xLabel = 70, xCampo = 200, gap = 50, alturaInput = 35;

        JLabel lblCed = new JLabel("Cédula:");
        lblCed.setBounds(xLabel, 240, 100, 30);
        lblCed.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tarjeta.add(lblCed);
        txtCedula = crearCampoEstiloLogin("V-12345678", xCampo, 240, 400, alturaInput);
        tarjeta.add(txtCedula);

        JLabel lblMonto = new JLabel("Monto:");
        lblMonto.setBounds(xLabel, 240 + gap, 100, 30);
        lblMonto.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tarjeta.add(lblMonto);
        txtMonto = crearCampoEstiloLogin("Monto en Bs.", xCampo, 240 + gap, 400, alturaInput);
        tarjeta.add(txtMonto);

        JLabel lblRef = new JLabel("Referencia:");
        lblRef.setBounds(xLabel, 240 + (2 * gap), 100, 30);
        lblRef.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tarjeta.add(lblRef);
        txtReferencia = crearCampoEstiloLogin("Últimos 4 dígitos", xCampo, 240 + (2 * gap), 400, alturaInput);
        tarjeta.add(txtReferencia);

        // Botones
        PrimaryButton2 btnCancelar = new PrimaryButton2("Cancelar", GRIS_CANCELAR);
        btnCancelar.setBounds(290, 440, 150, 45);
        btnCancelar.addActionListener(e -> {
            parentFrame.setVisible(true);
            dispose();
        });
        tarjeta.add(btnCancelar);

        PrimaryButton2 btnRecargar = new PrimaryButton2("Recargar", AMARILLO_BOTON);
        btnRecargar.setBounds(450, 440, 150, 45);
        btnRecargar.setForeground(Color.BLACK);
        btnRecargar.addActionListener(e -> procesarRecarga());
        tarjeta.add(btnRecargar);

        panelCentral.add(tarjeta, new GridBagConstraints());
        container.add(panelCentral, BorderLayout.CENTER);
        container.add(crearFooter(), BorderLayout.SOUTH);

        add(container);
    }

    /** Procesa la recarga usando el controlador y notifica al parentFrame */
private void procesarRecarga() {

    String cedulaInput = txtCedula.getText().trim();
    String montoInput = txtMonto.getText().trim();
    String referenciaInput = txtReferencia.getText().trim();

    PagoModel.ResultadoValidacion resultado =
            controlador.procesarPago(cedulaInput, montoInput, referenciaInput);

    switch (resultado) {

        case CAMPOS_INVALIDOS:
            JOptionPane.showMessageDialog(this,
                    "Campos inválidos.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            break;

        case PAGO_NO_ENCONTRADO:
            JOptionPane.showMessageDialog(this,
                    "Pago no encontrado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            break;

        case PAGO_YA_UTILIZADO:
            JOptionPane.showMessageDialog(this,
                    "Pago ya utilizado.",
                    "Atención",
                    JOptionPane.WARNING_MESSAGE);
            break;

        case MONTO_INCORRECTO:
            JOptionPane.showMessageDialog(this,
                    "Monto incorrecto.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            break;

        case RECARGA_EXITOSA:
            JOptionPane.showMessageDialog(this,
                "Recarga exitosa.",
                "Confirmación",
                JOptionPane.INFORMATION_MESSAGE);

            parentFrame.recargaExitosa();
            dispose();
            break;

        case ERROR_SISTEMA:
        default:
            JOptionPane.showMessageDialog(this,
                    "Error del sistema.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
    }
}

    private JTextField crearCampoEstiloLogin(String placeholder, int x, int y, int width, int height) {
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
        campo.setBackground(Color.WHITE);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        campo.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if(campo.getText().equals(placeholder)) campo.setText(""); }
            public void focusLost(FocusEvent e) { if(campo.getText().isEmpty()) campo.setText(placeholder); }
        });
        return campo;
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 50));
        JLabel lblCerrar = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lblCerrar.setForeground(Color.WHITE);
        lblCerrar.setFont(new Font("Arial", Font.PLAIN, 18));
        lblCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                DataBase dataBase = new DataBase();
                dataBase.LogedOut();
                dispose();
            }
        });
        footer.add(lblCerrar);
        return footer;
    }

    static class PanelRedondeado extends JPanel {
        private final int radio;
        private final Color color;
        public PanelRedondeado(int radio, Color color) { this.radio = radio; this.color = color; setOpaque(false); }
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