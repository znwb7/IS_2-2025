package com.ucv.view;

import com.ucv.model.DataBase;
import com.ucv.view.components.SideBar; // Sidebar versión usuario

import javax.swing.*;

import java.awt.*;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

public class PrincipalUsuario extends JFrame {
    DataBase dataBase = new DataBase();
    private final String usuarioID;

    LocalDate fechaActual = LocalDate.now();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    String fechaTexto = fechaActual.format(formatter);

    private final Color AZUL_FONDO = new Color(18, 71, 150);

    private final Color AZUL_ENCABEZADO = new Color(10, 45, 110);

    private final Color AZUL_BOTON = new Color(24, 116, 205);

    public PrincipalUsuario(String usuarioID) {
        this.usuarioID = usuarioID;
        System.out.println("ID del usuario en PrincipalUsuario: " + usuarioID); // Verificación de ID

        setTitle("Comedor UCV - Menú de Usuario");

        setSize(1920, 800);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        getContentPane().setBackground(AZUL_FONDO);

        setLayout(new BorderLayout());

        // --- 1. ENCABEZADO ---

        add(crearEncabezadoUsuario(), BorderLayout.NORTH);



// --- 2. CONTENEDOR INFERIOR ---

        JPanel contenedorInferior = new JPanel(new BorderLayout());

        contenedorInferior.setOpaque(false);

        contenedorInferior.add(new SideBar(this, usuarioID), BorderLayout.WEST);

// --- 3. PANEL DE CUERPO ---

        JPanel panelCuerpo = new JPanel(new GridBagLayout());

        panelCuerpo.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;

        gbc.fill = GridBagConstraints.HORIZONTAL;



        JLabel lblOpcion = new JLabel("Seleccione una opción");

        lblOpcion.setForeground(Color.WHITE);

        lblOpcion.setFont(new Font("Arial", Font.BOLD, 32));

        lblOpcion.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridy = 0;

        gbc.insets = new Insets(0, 0, 20, 0);

        panelCuerpo.add(lblOpcion, gbc);



        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 1;

        panelCuerpo.add(crearBotonUsuario("Ver menus"), gbc);

        gbc.gridy = 2;

        panelCuerpo.add(crearBotonUsuario("Entrar al comedor"), gbc);

        contenedorInferior.add(panelCuerpo, BorderLayout.CENTER);

        add(contenedorInferior, BorderLayout.CENTER);


// --- 4. FOOTER ---

        add(crearFooter(), BorderLayout.SOUTH);

    }

    private JPanel crearEncabezadoUsuario() {

        JPanel panelEncabezado = new JPanel(null) {

            @Override

            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(AZUL_ENCABEZADO);

                int alto = 140;

                g2.fillRoundRect(-30, 0, getWidth() + 60, alto, 60, 60);

                g2.fillRect(-30, 0, getWidth() + 60, alto / 2);

                g2.dispose();

            }

        };

        panelEncabezado.setPreferredSize(new Dimension(0, 150));

        panelEncabezado.setOpaque(false);



        JLabel lblTitulo = new JLabel("Comedor UCV");

        lblTitulo.setForeground(Color.WHITE);

        lblTitulo.setFont(new Font("Arial", Font.BOLD, 70));

        lblTitulo.setBounds(40, 15, 600, 80);



        JLabel lblFecha = new JLabel(fechaTexto);

        lblFecha.setForeground(new Color(210, 210, 210));

        lblFecha.setFont(new Font("Arial", Font.PLAIN, 22));

        lblFecha.setBounds(45, 85, 200, 30);



        panelEncabezado.add(lblTitulo);

        panelEncabezado.add(lblFecha);

        return panelEncabezado;

    }



    private JButton crearBotonUsuario(String texto) {

        JButton btn = new JButton(texto) {

            @Override

            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(AZUL_BOTON);

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 45, 45);

                g2.setColor(Color.WHITE);

                FontMetrics fm = g2.getFontMetrics();

                int x = (getWidth() - fm.stringWidth(getText())) / 2;

                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;

                g2.drawString(getText(), x, y);

                g2.dispose();

            }

        };



        btn.setFont(new Font("Arial", Font.BOLD, 28));

        btn.setPreferredSize(new Dimension(450, 80));

        btn.setContentAreaFilled(false);

        btn.setBorderPainted(false);

        btn.setFocusPainted(false);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

// --- LÓGICA DE NAVEGACIÓN ACTUALIZADA ---

        btn.addActionListener(e -> {

            
            if (texto.equals("Ver menus")) {

                new MenusUsuario(usuarioID).setVisible(true);
                dispose(); // Cerramos la ventana actual

            } else if (texto.equals("Entrar al comedor")) {

                if (dataBase.GetFoodFlag(dataBase.ReturnID()).equals("1")){

                    new VerificacionFacialUCV(usuarioID).setVisible(true);
                    dispose(); // Cerramos la ventana actual

                }else{

                    javax.swing.JOptionPane.showMessageDialog(
                        null, 
                        "Antes Debe Generar un Turno", 
                        "Aviso del Sistema", 
                        javax.swing.JOptionPane.WARNING_MESSAGE
                    );

                }
                

            }

        });



        return btn;

    }



    private JPanel crearFooter() {

        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        panelFooter.setOpaque(false);

        panelFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 40));



        JLabel lblCerrar = new JLabel("<html><u>Cerrar Sesión</u></html>");

        lblCerrar.setForeground(Color.WHITE);

        lblCerrar.setFont(new Font("Arial", Font.PLAIN, 20));

        lblCerrar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lblCerrar.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override

            public void mouseClicked(java.awt.event.MouseEvent e) {
                DataBase dataBase = new DataBase();
                dataBase.LogedOut();
                dispose();
                com.ucv.ComedorApp.main(null);

            }

        });

        panelFooter.add(lblCerrar);

        return panelFooter;

    }
}