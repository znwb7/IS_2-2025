package com.ucv.view;

import com.ucv.controller.UserController;
import com.ucv.view.components.PrimaryButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegistroUCV extends JFrame {

    private final UserController userController;
    private static final Color AZUL_UCV = new Color(18, 71, 150);
    private static final Color GRIS_INPUT = new Color(235, 235, 235);

    public RegistroUCV(UserController controller) {
        this.userController = controller;

        setTitle("Registro · Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // --- ENCABEZADO (LOGO) ---
        JPanel panelLogo = new JPanel();
        panelLogo.setBackground(Color.WHITE);
        panelLogo.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));

        JLabel logo = new JLabel();
        try {
            java.net.URL res = getClass().getResource("/com/ucv/view/logo_comedor.png");
            if (res != null) {
                Image img = new ImageIcon(res).getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                logo.setIcon(new ImageIcon(img));
            } else {
                logo.setText("Comedor UCV");
            }
        } catch (Exception e) {
            logo.setText("Comedor UCV");
        }

        panelLogo.add(logo);
        add(panelLogo, BorderLayout.NORTH);

        // --- CUERPO ---
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        // Título posicionado exactamente igual al Login
        JLabel titulo = new JLabel("Comedor UCV");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 1, 0);
        center.add(titulo, gbc);

        // --- TARJETA AZUL (Ajustada para 4 campos + PrimaryButton) ---
        RoundedPanel card = new RoundedPanel(50, AZUL_UCV);
        card.setPreferredSize(new Dimension(480, 360));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(15, 40, 1, 40));

        JLabel lblRegistro = new JLabel("Registrarse");
        lblRegistro.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblRegistro.setForeground(Color.WHITE);
        lblRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblRegistro);
        card.add(Box.createVerticalStrut(20));

        // Campos de texto
        JTextField campoNombre = crearCampo("Nombre", false);
        JTextField campoApellido = crearCampo("Apellido", false);
        JTextField campoCedula = crearCampo("Cédula", false);
        JPasswordField campoPass = (JPasswordField) crearCampo("Contraseña", true);

        card.add(campoNombre);
        card.add(Box.createVerticalStrut(10));
        card.add(campoApellido);
        card.add(Box.createVerticalStrut(10));
        card.add(campoCedula);
        card.add(Box.createVerticalStrut(10));
        card.add(campoPass);
        card.add(Box.createVerticalStrut(25));

        // BOTÓN REGISTRARSE (Componente PrimaryButton)
        PrimaryButton btnRegistrar = new PrimaryButton("Registrarse");
        Dimension sizeBoton = new Dimension(280, 37); // AQUÍ CONTROLAS EL TAMAÑO
        btnRegistrar.setMaximumSize(sizeBoton);
        btnRegistrar.setMinimumSize(sizeBoton);
        btnRegistrar.setPreferredSize(sizeBoton);
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(btnRegistrar);

        card.add(Box.createVerticalStrut(15));
        // Link para volver al Login
        JLabel linkLogin = new JLabel("<html>¿Ya tienes cuenta? <u>Iniciar Sesión</u></html>");
        linkLogin.setForeground(Color.WHITE);
        linkLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        linkLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        linkLogin.setBorder(BorderFactory.createEmptyBorder(-4, 100, 0, 20));
        card.add(linkLogin);
        card.add(Box.createVerticalGlue());

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        center.add(card, gbc);

        add(center, BorderLayout.CENTER);

        // --- EVENTOS ---
        btnRegistrar.addActionListener(e -> {
            String nombre = campoNombre.getText().equals("Nombre") ? "" : campoNombre.getText();
            String apellido = campoApellido.getText().equals("Apellido") ? "" : campoApellido.getText();
            String cedula = campoCedula.getText().equals("Cédula") ? "" : campoCedula.getText();
            String pass = new String(campoPass.getPassword()).equals("Contraseña") ? "" : new String(campoPass.getPassword());

            if (userController != null) {
                UserController.Response response = userController.register(nombre, apellido, cedula, pass);
                JOptionPane.showMessageDialog(this, response.getMessage());
                if (response.isSuccess()) {
                    abrirLogin();
                }
            }
        });

        linkLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirLogin();
            }
        });
    }

    private void abrirLogin() {
        LoginUCV login = new LoginUCV();
        // Asegúrate de que LoginUCV tenga el método setController
        login.setController(userController);
        login.setVisible(true);
        dispose();
    }

    private JTextField crearCampo(String placeholder, boolean password) {
        JTextField campo = password ? new JPasswordField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        } : new JTextField(placeholder) {
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
        if (password) ((JPasswordField) campo).setEchoChar((char) 0);
        campo.setMaximumSize(new Dimension(600, 42));
        campo.setBackground(GRIS_INPUT);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(placeholder)) {
                    campo.setText("");
                    if (password) ((JPasswordField) campo).setEchoChar('•');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(placeholder);
                    if (password) ((JPasswordField) campo).setEchoChar((char) 0);
                }
            }
        });
        return campo;
    }

    static class RoundedPanel extends JPanel {
        private final int radius;
        private final Color color;
        public RoundedPanel(int radius, Color color) {
            this.radius = radius;
            this.color = color;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        }
    }
}