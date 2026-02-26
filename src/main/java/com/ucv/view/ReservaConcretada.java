package com.ucv.view;

import com.ucv.model.DataBase;
import com.ucv.view.components.HeaderUCV;
import com.ucv.view.components.SideBar;
import javax.swing.*;
import java.awt.*;

public class ReservaConcretada extends JFrame {

    private static final Color AZUL_FONDO = new Color(18, 71, 150);
    private static final Color AMARILLO_TEXTO = new Color(255, 210, 35);

    public ReservaConcretada(String tipoMenuSeleccionado) {
        setTitle("Reserva Concretada · Comedor UCV");
        setSize(1920, 1080);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        DataBase dataBase = new DataBase();
        dataBase.MenuActive(dataBase.ReturnID());


        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AZUL_FONDO);

        // Componentes reutilizables
        container.add(new HeaderUCV(), BorderLayout.NORTH);
        container.add(new SideBar(this), BorderLayout.WEST);

        // --- PANEL CENTRAL (Espejo de la imagen) ---
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // 1. Mensaje de éxito
        JLabel lblExito = new JLabel("<html><center>Ha concretado su reserva<br>para el menú</center></html>");
        lblExito.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblExito.setForeground(Color.WHITE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        panelCentral.add(lblExito, gbc);

        // 2. Tipo de Menú (Dinámico y en Negrita)
        JLabel lblMenu = new JLabel(tipoMenuSeleccionado);
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblMenu.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        panelCentral.add(lblMenu, gbc);

        // 3. Instrucción en Amarillo
        JLabel lblInstruccion = new JLabel("<html><center>Diríjase al comedor en el horario<br>seleccionado para recibir su comida.</center></html>");
        lblInstruccion.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblInstruccion.setForeground(AMARILLO_TEXTO);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 40, 0);
        panelCentral.add(lblInstruccion, gbc);

        // 4. Despedida
        JLabel lblProvecho = new JLabel("¡BUEN PROVECHO!");
        lblProvecho.setFont(new Font("Segoe UI", Font.ITALIC | Font.BOLD, 30));
        lblProvecho.setForeground(Color.WHITE);
        gbc.gridy = 3;
        panelCentral.add(lblProvecho, gbc);

        container.add(panelCentral, BorderLayout.CENTER);
        container.add(crearFooter(), BorderLayout.SOUTH);

        add(container);
    }

    private JPanel crearFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 40));
        JLabel lblCerrar = new JLabel("<html><u>Cerrar Sesión</u></html>");
        lblCerrar.setForeground(Color.WHITE);
        lblCerrar.setFont(new Font("Arial", Font.PLAIN, 18));
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
        footer.add(lblCerrar);
        return footer;
    }

    public static void main(String[] args) {
        // Ejemplo: Se abre al confirmar un "Almuerzo"
        SwingUtilities.invokeLater(() -> new ReservaConcretada("Almuerzo").setVisible(true));
    }
}