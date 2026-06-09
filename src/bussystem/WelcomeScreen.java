package bussystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WelcomeScreen extends JFrame {
    private JButton btnAdminLogin, btnUserLogin;

    public WelcomeScreen() {
        setTitle("Bus Seat Reservation System - Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10)); // Only 3 rows needed: Title + 2 buttons

        JLabel titleLabel = new JLabel("Welcome to Bus Reservation System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

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
