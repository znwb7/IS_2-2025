package com.ucv.view;

import com.ucv.controller.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegistroUCV extends JFrame {

    private final Color AZUL_UCV = new Color(22, 75, 150);
    private final Color NARANJA_UCV = new Color(242, 160, 27);
    private final Color GRIS_FONDO_INPUT = new Color(211, 211, 211);

    private final UserController userController;

    public RegistroUCV(UserController controller) {
        this.userController = controller;
        setTitle("Registro - Comedor UCV");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 10, 0));

            JLabel lblLogo = new JLabel();
            try {
                java.net.URL res = getClass().getResource("/com/ucv/view/logo_comedor.png");
                if (res != null) {
                    Image img = new ImageIcon(res).getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                    lblLogo.setIcon(new ImageIcon(img));
                } else {
                    lblLogo.setText("Comedor UCV");
                }
            } catch (Exception e) {
                lblLogo.setText("Comedor UCV");
            }
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel lblTitulo = new JLabel("Comedor UCV");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        topPanel.add(lblLogo);
        topPanel.add(lblTitulo);
        add(topPanel, BorderLayout.NORTH);

        // Panel azul
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.WHITE);

        JPanel blueCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AZUL_UCV);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),35,35);
                g2.dispose();
            }
        };
        blueCard.setPreferredSize(new Dimension(380,520));
        blueCard.setLayout(new GridBagLayout());
        blueCard.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 25, 8, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel lblRegistrarse = new JLabel("Registrarse", SwingConstants.CENTER);
        lblRegistrarse.setForeground(Color.WHITE);
        lblRegistrarse.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 0;
        gbc.insets = new Insets(20,25,15,25);
        blueCard.add(lblRegistrarse, gbc);

        // Campos de texto
        JTextField campoNombre = crearCampoConPlaceholder(blueCard, gbc, "Nombre", 1, false);
        JTextField campoApellido = crearCampoConPlaceholder(blueCard, gbc, "Apellido", 2, false);
        JTextField campoCedula = crearCampoConPlaceholder(blueCard, gbc, "Cedula", 3, false);
        JPasswordField campoPassword = (JPasswordField) crearCampoConPlaceholder(blueCard, gbc, "Contraseña", 4, true);

        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrar.setBackground(NARANJA_UCV);
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setPreferredSize(new Dimension(200,45));
        gbc.gridy = 5;
        gbc.insets = new Insets(25,60,10,60);
        blueCard.add(btnRegistrar, gbc);

        // Evento de registro
        btnRegistrar.addActionListener(e -> {
            String nombre = campoNombre.getText();
            String apellido = campoApellido.getText();
            String cedula = campoCedula.getText();
            String password = new String(campoPassword.getPassword());

           UserController.Response response =
        userController.register(nombre, apellido, cedula, password);

JOptionPane.showMessageDialog(this, response.getMessage());

if (response.isSuccess()) {
    dispose();
    LoginUCV login = new LoginUCV();
    login.setController(userController);
    login.setVisible(true);
}
        });

        JLabel lblLoginLink = new JLabel("<html><u>Iniciar Sesión</u></html>", SwingConstants.CENTER);
        lblLoginLink.setForeground(Color.WHITE);
        lblLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
lblLoginLink.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        LoginUCV login = new LoginUCV();
        login.setController(userController); // ← CLAVE
        login.setVisible(true);
        dispose();
    }
});
        gbc.gridy = 6;
        gbc.insets = new Insets(5,25,20,25);
        blueCard.add(lblLoginLink, gbc);

        wrapperPanel.add(blueCard);
        add(wrapperPanel, BorderLayout.CENTER);
    }

    private JTextField crearCampoConPlaceholder(JPanel panel, GridBagConstraints gbc, String texto, int fila, boolean esPassword) {
        JTextField campo;
        if (esPassword) {
            JPasswordField pf = new JPasswordField(texto);
            pf.setEchoChar((char)0);
            campo = pf;
        } else {
            campo = new JTextField(texto);
        }

        campo.setBackground(GRIS_FONDO_INPUT);
        campo.setForeground(Color.GRAY);
        campo.setFont(new Font("Arial", Font.PLAIN, 15));
        campo.setPreferredSize(new Dimension(300,40));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRIS_FONDO_INPUT,1),
                BorderFactory.createEmptyBorder(5,10,5,10)
        ));

        campo.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(texto)) {
                    campo.setText("");
                    campo.setForeground(Color.BLACK);
                    if (esPassword) ((JPasswordField)campo).setEchoChar('•');
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(texto);
                    campo.setForeground(Color.GRAY);
                    if (esPassword) ((JPasswordField)campo).setEchoChar((char)0);
                }
            }
        });

        gbc.gridy = fila;
        panel.add(campo, gbc);
        return campo;
    }
}
