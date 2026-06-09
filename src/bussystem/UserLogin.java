package bussystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserLogin extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;

    public UserLogin() {
        setTitle("User Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel userIdLabel = new JLabel("User ID:");
        userIdField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        add(userIdLabel);
        add(userIdField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText().trim());
                String password = new String(passwordField.getPassword());

                if (DatabaseOperations.validateUserLogin(userId, password)) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    dispose();
                    SwingUtilities.invokeLater(() -> new UserDashboard(userId).setVisible(true));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Credentials!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid numeric User ID.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        registerButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new RegisterUser().setVisible(true));
        });

        setVisible(true);
    }
}
