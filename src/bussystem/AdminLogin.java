package bussystem;

import javax.swing.*;
import java.awt.*;

public class AdminLogin extends JFrame {
    public AdminLogin() {
        setTitle("Admin Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel lblAdminID = new JLabel("Admin ID:");
        JTextField txtAdminID = new JTextField();
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField();
        JButton btnLogin = new JButton("Login");

        add(lblAdminID);
        add(txtAdminID);
        add(lblPassword);
        add(txtPassword);
        add(new JLabel()); // Empty cell
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String adminID = txtAdminID.getText();
            String password = new String(txtPassword.getPassword());

            if (adminID.equals("admin") && password.equals("admin123")) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new AdminPanel();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin ID or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true); // 🚨 Must be here to show the login page
    }
}
