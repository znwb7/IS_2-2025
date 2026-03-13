package com.ucv.view.comedor;

import com.ucv.controller.FacialController;
import com.ucv.view.components.HeaderUCV;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class VerificacionFacialUCV extends JFrame {

    private final Color AZUL_FONDO = new Color(18, 71, 150);
    private final Color GRIS_TARJETA = new Color(211, 211, 211);
    private final Color GRIS_CIRCULO = new Color(190, 190, 190);

    private String usuarioID;
    private JTextField inputID;

    public VerificacionFacialUCV() {

        setTitle("Verificación Facial - Comedor UCV");
        setSize(1920, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        container.add(new HeaderUCV(), BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblTitulo = new JLabel("Verificación facial");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);

        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,20,0);
        panelCentral.add(lblTitulo, gbc);

        // -------- INPUT DE ID --------

        JPanel panelInput = new JPanel(new BorderLayout());
        panelInput.setOpaque(false);
        panelInput.setPreferredSize(new Dimension(320,60));

        JLabel lblID = new JLabel("Ingrese su cédula:");
        lblID.setForeground(Color.WHITE);
        lblID.setFont(new Font("Segoe UI", Font.BOLD, 16));

        inputID = new JTextField();
        inputID.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        panelInput.add(lblID, BorderLayout.NORTH);
        panelInput.add(inputID, BorderLayout.CENTER);

        gbc.gridy = 1;
        gbc.insets = new Insets(0,0,20,0);
        panelCentral.add(panelInput, gbc);

        // -------- PANEL DE CARGA --------

        JPanel panelCuadro = crearPanelCargaVisual();

        gbc.gridy = 2;
        gbc.insets = new Insets(10,0,20,0);
        panelCentral.add(panelCuadro, gbc);

        JLabel lblFooterText = new JLabel("Solo se aceptan archivos en formato .jpg");
        lblFooterText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblFooterText.setForeground(new Color(180,190,210));

        gbc.gridy = 4;
        gbc.insets = new Insets(20,0,0,0);
        panelCentral.add(lblFooterText, gbc);

        container.add(panelCentral, BorderLayout.CENTER);
        add(container);
    }

    private JPanel crearPanelCargaVisual() {

        JPanel p = new JPanel(null) {

            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(GRIS_TARJETA);
                g2.fill(new RoundRectangle2D.Double(
                        0,0,getWidth(),getHeight(),20,20));

                g2.setColor(GRIS_CIRCULO);

                int diametro = 180;
                int x = (getWidth() - diametro)/2;
                int y = 90;

                g2.fill(new Ellipse2D.Double(x,y,diametro,diametro));

                g2.dispose();
            }
        };

        p.setPreferredSize(new Dimension(320,310));
        p.setOpaque(false);

        JLabel lblCargar = new JLabel("Cargar imagen", SwingConstants.CENTER);

        lblCargar.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblCargar.setBounds(0,35,320,30);

        p.add(lblCargar);

        JLabel btnPlus = new JLabel("+", SwingConstants.CENTER);

        btnPlus.setFont(new Font("Arial", Font.PLAIN, 85));
        btnPlus.setForeground(new Color(60,60,60));
        btnPlus.setBounds(0,90,320,180);
        btnPlus.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPlus.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent e) {

                usuarioID = inputID.getText().trim();

                if(usuarioID.isEmpty()){

                    JOptionPane.showMessageDialog(
                            VerificacionFacialUCV.this,
                            "Debe ingresar su cédula antes de cargar la imagen.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                    return;
                }

                FacialController facialController = new FacialController();

                com.ucv.controller.UserController.Response respuesta =
                        facialController.LoadImage(usuarioID);

                if(respuesta.isSuccess()){
                    dispose();
                    new VerificacionFacialExitosa().setVisible(true);
                }else {

                    JOptionPane.showMessageDialog(
                            VerificacionFacialUCV.this,
                            respuesta.getMessage(),
                            "Verificación de Identidad",
                            JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });

        p.add(btnPlus);

        return p;
    }
}