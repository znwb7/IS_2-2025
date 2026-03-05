package com.ucv.view;

import com.ucv.controller.UserController;
import com.ucv.model.DataBase;
import com.ucv.view.components.PrimaryButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginUCV extends JFrame {

    private UserController controller;
    public void setController(UserController controller) {
        this.controller = controller;
    }

    private static final Color AZUL_UCV = new Color(18, 71, 150);
    private static final Color GRIS_INPUT = new Color(235, 235, 235);

    public LoginUCV() {

        setTitle("Login · Comedor UCV");
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

        JLabel titulo = new JLabel("Comedor UCV");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        gbc.gridy = 0;
        gbc.insets = new Insets(-100, 0, 90, 0);
        center.add(titulo, gbc);

        // --- TARJETA AZUL ---
        RoundedPanel card = new RoundedPanel(50, AZUL_UCV);
        card.setPreferredSize(new Dimension(480, 300));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 40, 30, 40));

        JLabel lblLogin = new JLabel("Iniciar Sesión");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblLogin);
        card.add(Box.createVerticalStrut(15));

        // --- CAMPO CÉDULA Y AYUDA ---
        JTextField campoCedula = crearCampo("Cédula", false);
        campoCedula.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(campoCedula);

        JLabel ayudaCedula = new JLabel("Coloque solo números sin puntos, letras u caracteres especiales. Ej: 12345678");
        ayudaCedula.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ayudaCedula.setForeground(new Color(225, 225, 225));
        ayudaCedula.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Ajuste de margen para que el texto sea visible y empiece a la izquierda
        ayudaCedula.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        card.add(ayudaCedula);

        card.add(Box.createVerticalStrut(8));

        // --- CAMPO CONTRASEÑA Y AYUDA ---
        JPasswordField campoPass = (JPasswordField) crearCampo("Contraseña", true);
        campoPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(campoPass);

        JLabel ayudaPass = new JLabel("Debe tener mínimo tres caracteres (Letras y/o Números)");
        ayudaPass.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        ayudaPass.setForeground(new Color(225, 225, 225));
        ayudaPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Ajuste de margen para alinear a la izquierda sin recortar el texto
        ayudaPass.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 115));
        card.add(ayudaPass);

        card.add(Box.createVerticalStrut(15));

        // --- BOTÓN ---
        PrimaryButton btnLogin = new PrimaryButton("Ingresar");
        // Aseguramos el centrado explícito del botón
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(btnLogin);

        card.add(Box.createVerticalStrut(10));

        // --- ENLACE DE REGISTRO CENTRADO ---
        JLabel linkRegistro = new JLabel("<html>¿No tienes cuenta? <u>Regístrate</u></html>");
        linkRegistro.setForeground(Color.WHITE);
        linkRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        linkRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Centrado respecto al botón "Ingresar"
        linkRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        linkRegistro.setBorder(BorderFactory.createEmptyBorder(0, 100, 0, 0));
        card.add(linkRegistro);

        gbc.gridy = 1;
        gbc.insets = new Insets(-70, 0, 0, 0);
        center.add(card, gbc);

        add(center, BorderLayout.CENTER);

        // --- EVENTOS ---
        btnLogin.addActionListener(e -> {
            if (controller != null) {
                String id = campoCedula.getText().equals("Cédula") ? "" : campoCedula.getText();
                String pass = new String(campoPass.getPassword()).equals("Contraseña")
                        ? "" : new String(campoPass.getPassword());
                controller.loginRequested(id, pass, this);
                DataBase dataBase = new DataBase();
                dataBase.LogedIn(id);
            }
        });

        linkRegistro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegistroUCV registro = new RegistroUCV(controller);
                registro.setVisible(true);
                dispose();
            }
        });
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
        campo.setMaximumSize(new Dimension(600, 50));
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