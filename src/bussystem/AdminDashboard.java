package bussystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    private JButton btnManageBuses, btnManageUsers, btnManageReservations, btnManageWaitlist, btnLogout;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        btnManageBuses = new JButton("Manage Buses");
        btnManageUsers = new JButton("Manage Users");
        btnManageReservations = new JButton("Manage Reservations");
        btnManageWaitlist = new JButton("Manage Waitlist");
        btnLogout = new JButton("Logout");

        panel.add(btnManageBuses);
        panel.add(btnManageUsers);
        panel.add(btnManageReservations);
        panel.add(btnManageWaitlist);
        panel.add(btnLogout);

        add(panel);

        // Button Actions
        btnManageBuses.addActionListener(e -> {
            new ManageBuses();
            dispose();
        });

        btnManageUsers.addActionListener(e -> {
            new ManageUsers();
            dispose();
        });

        btnManageReservations.addActionListener(e -> {
            new ManageReservations();
            dispose();
        });

        btnManageWaitlist.addActionListener(e -> {
            new ManageWaitlist();
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new AdminLogin();
            dispose();
        });

        setVisible(true);
    }
}
