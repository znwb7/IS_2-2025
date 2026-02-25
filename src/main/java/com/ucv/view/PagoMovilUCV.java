package com.ucv.view;

import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SIdeBar2;
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

    // Variables para los campos para poder validarlos
    private JTextField txtCedula, txtMonto, txtReferencia;

    public PagoMovilUCV() {
        setTitle("Pago Móvil · Comedor UCV");
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

        // Tarjeta Principal
        PanelRedondeado tarjeta = new PanelRedondeado(40, GRIS_TARJETA);
        tarjeta.setPreferredSize(new Dimension(700, 500));
        tarjeta.setLayout(null);

        // --- SECCIÓN SUPERIOR: DATOS BANCARIOS ---
        JLabel lblTituloPago = new JLabel("Realiza tu pago móvil a:");
        lblTituloPago.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloPago.setForeground(AZUL_TEXTO);
        lblTituloPago.setBounds(50, 30, 400, 40);
        tarjeta.add(lblTituloPago);

        PanelRedondeado infoBlanca = new PanelRedondeado(20, Color.WHITE);
        infoBlanca.setBounds(50, 80, 600, 130);
        infoBlanca.setLayout(null);

        String[][] datos = {
                {"Banco:", "MERCANTIL"},
                {"RIF:", "J-12345678"},
                {"Teléfono:", "0412-1234567"}
        };

        for (int i = 0; i < datos.length; i++) {
            JLabel k = new JLabel(datos[i][0]);
            k.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            k.setBounds(30, 20 + (i * 30), 200, 25);
            infoBlanca.add(k);

            JLabel v = new JLabel(datos[i][1]);
            v.setFont(new Font("Segoe UI", Font.BOLD, 18));
            v.setBounds(250, 20 + (i * 30), 320, 25);
            v.setHorizontalAlignment(SwingConstants.RIGHT);
            infoBlanca.add(v);
        }
        tarjeta.add(infoBlanca);

        // --- SECCIÓN INFERIOR: FORMULARIO ---
        JLabel lblInstruccion = new JLabel("Ingresa los datos de tu Pago Móvil");
        lblInstruccion.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblInstruccion.setForeground(AZUL_TEXTO);
        lblInstruccion.setBounds(50, 235, 400, 30);
        tarjeta.add(lblInstruccion);

        int xLabel = 70, xCampo = 200, yBase = 280, gap = 50, alturaInput = 35;

        // Cédula
        JLabel lblCed = new JLabel("Cédula:");
        lblCed.setBounds(xLabel, yBase, 100, 30);
        lblCed.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tarjeta.add(lblCed);
        txtCedula = crearCampoEstiloLogin("V-12345678", xCampo, yBase, 400, alturaInput);
        tarjeta.add(txtCedula);

        // Monto
        JLabel lblMon = new JLabel("Monto:");
        lblMon.setBounds(xLabel, yBase + gap, 100, 30);
        lblMon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tarjeta.add(lblMon);
        txtMonto = crearCampoEstiloLogin("Monto en Bs.", xCampo, yBase + gap, 400, alturaInput);
        tarjeta.add(txtMonto);

        // Referencia
        JLabel lblRef = new JLabel("Referencia:");
        lblRef.setBounds(xLabel, yBase + (gap * 2), 100, 30);
        lblRef.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tarjeta.add(lblRef);
        txtReferencia = crearCampoEstiloLogin("Últimos 4 dígitos", xCampo, yBase + (gap * 2), 400, alturaInput);
        tarjeta.add(txtReferencia);

        // --- BOTONES ---
        // BOTÓN CANCELAR
        PrimaryButton2 btnCanc = new PrimaryButton2("Cancelar", GRIS_CANCELAR);
        btnCanc.setBounds(290, 440, 150, 45);
        btnCanc.addActionListener(e -> {
            new BilleteraUCV().setVisible(true);
            dispose();
        });
        tarjeta.add(btnCanc);

        // BOTÓN RECARGAR CON LÓGICA DE ÉXITO
        PrimaryButton2 btnRec = new PrimaryButton2("Recargar", AMARILLO_BOTON);
        btnRec.setBounds(450, 440, 150, 45);
        btnRec.setForeground(Color.BLACK);

        btnRec.addActionListener(e -> {
            // Validación simple: verificar que no estén los placeholders
            if (txtCedula.getText().equals("V-12345678") || txtMonto.getText().equals("Monto en Bs.") ||
                    txtReferencia.getText().equals("Últimos 4 dígitos")) {

                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos correctamente.",
                        "Error de Validación", JOptionPane.WARNING_MESSAGE);
            } else {
                // Mensaje de éxito
                JOptionPane.showMessageDialog(this, "Recarga Exitosa",
                        "Confirmación", JOptionPane.INFORMATION_MESSAGE);

                // Redirección al Menú Principal
                new PrincipalUsuario("Estudiante").setVisible(true);
                dispose();
            }
        });

        tarjeta.add(btnRec);

        panelCentral.add(tarjeta, new GridBagConstraints());
        container.add(panelCentral, BorderLayout.CENTER);
        container.add(crearFooter(), BorderLayout.SOUTH);

        add(container);
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
                dispose();
                // Aquí podrías llamar a tu Login inicial si lo tienes
            }
        });
        footer.add(lblCerrar);
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
        SwingUtilities.invokeLater(() -> new PagoMovilUCV().setVisible(true));
    }
}