package bussystem;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private JButton btnAdminLogin, btnUserLogin;

    public MainMenu() {
        setTitle("Bus Seat Reservation System - Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel titleLabel = new JLabel("Welcome to Bus Reservation System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        btnAdminLogin = new JButton("Admin Login");
        btnUserLogin = new JButton("User Login");

        add(titleLabel);
        add(btnAdminLogin);
        add(btnUserLogin);

        btnAdminLogin.addActionListener(e -> {
            new AdminLogin();
            dispose();
        });

        btnUserLogin.addActionListener(e -> {
            new UserLogin(); 
            dispose();
        });

        setVisible(true); 
    }
}
