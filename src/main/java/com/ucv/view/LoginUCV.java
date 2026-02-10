package com.ucv.view;

import com.ucv.controller.UserController;
import com.ucv.controller.UserController.Response;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginUCV extends JFrame {

    private final UserController controller = new UserController();

    private static final Color AZUL_UCV = new Color(18, 71, 150);
    private static final Color NARANJA_UCV = new Color(250, 168, 44);
    private static final Color GRIS_INPUT = new Color(235, 235, 235);

    public LoginUCV() {

        setTitle("Login · Comedor UCV");
        setSize(520, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel panelLogo = new JPanel();
        panelLogo.setBackground(Color.WHITE);
        panelLogo.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));

        JLabel logo = new JLabel();
        try {
            Image img = new ImageIcon("logoucv.png")
                    .getImage()
                    .getScaledInstance(170, 170, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            logo.setText("LOGO UCV");
        }

        panelLogo.add(logo);
        add(panelLogo, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        JLabel titulo = new JLabel("Comedor UCV");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 30, 0);
        center.add(titulo, gbc);

        RoundedPanel card = new RoundedPanel(35, AZUL_UCV);
        card.setPreferredSize(new Dimension(380, 460));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));

        JLabel lblLogin = new JLabel("Iniciar Sesión");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblLogin.setForeground(Color.WHITE);
        lblLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblLogin);
        card.add(Box.createVerticalStrut(35));

        JTextField campoCedula = crearCampo("Cédula", false);
        JPasswordField campoPass = (JPasswordField) crearCampo("Contraseña", true);

        card.add(campoCedula);
        card.add(Box.createVerticalStrut(18));
        card.add(campoPass);
        card.add(Box.createVerticalStrut(35));

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setBackground(NARANJA_UCV);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        card.add(btnLogin);
        card.add(Box.createVerticalStrut(18));

        JLabel lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lblMensaje);

        card.add(Box.createVerticalStrut(20));

        JLabel linkRegistro = new JLabel(
                "<html>¿No tienes cuenta? <u>Regístrate</u></html>");
        linkRegistro.setForeground(Color.WHITE);
        linkRegistro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        linkRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(linkRegistro);

        gbc.gridy = 1;
        center.add(card, gbc);

        add(center, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> {
            String id = campoCedula.getText().equals("Cédula") ? "" : campoCedula.getText();
            String pass = new String(campoPass.getPassword()).equals("Contraseña")
                    ? "" : new String(campoPass.getPassword());

            Response r = controller.login(id, pass);

            if (r.isSuccess()) {
                controller.ejecutarRedireccion(id, this);
            } else {
                lblMensaje.setText(r.getMessage());
                lblMensaje.setForeground(Color.RED);
            }
        });

        linkRegistro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegistroUCV().setVisible(true);
                dispose();
            }
        });
    }

    private JTextField crearCampo(String placeholder, boolean password) {
        JTextField campo = password ? new JPasswordField(placeholder) : new JTextField(placeholder);
        if (password) ((JPasswordField) campo).setEchoChar((char) 0);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        campo.setBackground(GRIS_INPUT);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUCV().setVisible(true));
    }
}