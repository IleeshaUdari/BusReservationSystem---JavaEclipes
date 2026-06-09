package bussystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterUser extends JFrame {
    private JTextField nameField, passwordField, emailField, phoneField, cityField, ageField;

    public RegisterUser() {
        setTitle("User Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(8, 2, 10, 10));
        
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Password:"));
        passwordField = new JTextField();
        add(passwordField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("City:"));
        cityField = new JTextField();
        add(cityField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        JButton registerButton = new JButton("Register");
        add(registerButton);

        JButton backButton = new JButton("Back");
        add(backButton);

        registerButton.addActionListener(this::btnRegisterActionPerformed);
        backButton.addActionListener(e -> {
            dispose();
            new UserLogin();
        });

        setVisible(true);
    }

    private void btnRegisterActionPerformed(ActionEvent evt) {
        try {
            String name = nameField.getText().trim();
            String password = passwordField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String city = cityField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());

            boolean success = DatabaseOperations.registerUser(name, password, email, phone, city, age);

            if (success) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                clearForm();
                dispose();
                new UserLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Registration Failed. Please try again.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearForm() {
        nameField.setText("");
        passwordField.setText("");
        emailField.setText("");
        phoneField.setText("");
        cityField.setText("");
        ageField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegisterUser::new);
    }
}
