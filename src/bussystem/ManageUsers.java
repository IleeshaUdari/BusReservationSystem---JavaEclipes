package bussystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageUsers extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton refreshButton, deleteButton, btnReserve;
    private JPanel buttonPanel;

    public ManageUsers() {
        setTitle("Manage Users");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"User ID", "Name", "Email", "Phone", "City", "Age"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        refreshButton = new JButton("Refresh Users");
        deleteButton = new JButton("Delete Selected User");
        btnReserve = new JButton("Make Reservation");
        buttonPanel = new JPanel();

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(btnReserve);

        refreshButton.addActionListener(e -> loadUsers());
        deleteButton.addActionListener(e -> deleteSelectedUser());
        btnReserve.addActionListener(e -> openReservationPanel());

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUsers();  
    }

    private void loadUsers() {
        model.setRowCount(0);
        try {
            ResultSet rs = DatabaseOperations.loadUsers();
            boolean found = false;
            while (rs != null && rs.next()) {
                int userId = rs.getInt("user_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String city = rs.getString("city");
                int age = rs.getInt("age");
                model.addRow(new Object[]{userId, name, email, phone, city, age});
                found = true;
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "No users found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) model.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = DatabaseOperations.deleteUser(userId);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully.");
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }

    private void openReservationPanel() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) model.getValueAt(selectedRow, 0);
            new ReservationPanel(userId).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to make a reservation.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageUsers().setVisible(true));
    }
}
