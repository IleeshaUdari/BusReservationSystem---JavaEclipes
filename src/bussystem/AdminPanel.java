package bussystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * AdminPanel.java
 * Admin dashboard with buttons that launch separate management pages.
 */
public class AdminPanel extends JFrame {
    private JButton btnManageBuses, btnManageUsers, btnManageReservations, btnManageWaitlist;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel titleLabel = new JLabel("Admin Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        btnManageBuses = new JButton("Manage Buses");
        btnManageUsers = new JButton("Manage Users");
        btnManageReservations = new JButton("Manage Reservations");
        btnManageWaitlist = new JButton("Manage Waitlist");

        add(titleLabel);
        add(btnManageBuses);
        add(btnManageUsers);
        add(btnManageReservations);
        add(btnManageWaitlist);

        // Actions for each button
        btnManageBuses.addActionListener(e -> {
            try {
                new ManageBuses().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening Manage Buses: " + ex.getMessage());
            }
        });

        btnManageUsers.addActionListener(e -> {
            try {
                new ManageUsers().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening Manage Users: " + ex.getMessage());
            }
        });

        btnManageReservations.addActionListener(e -> {
            try {
                new ManageReservations().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening Manage Reservations: " + ex.getMessage());
            }
        });

        btnManageWaitlist.addActionListener(e -> {
            try {
                new ManageWaitlist().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error opening Manage Waitlist: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    /**
     * Adds a new reservation to the database.
     */
    public static void addReservation(int userId, int busId, int seatsBooked, int totalFare, String date) throws SQLException {
        String sql = "INSERT INTO reservations (user_id, bus_id, reserved_seats, total_fare, reservation_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, busId);
            ps.setInt(3, seatsBooked);
            ps.setInt(4, totalFare);
            ps.setString(5, date);
            ps.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel().setVisible(true));
    }
}
