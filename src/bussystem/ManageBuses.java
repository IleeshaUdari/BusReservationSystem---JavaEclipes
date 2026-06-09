package bussystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * ManageBuses.java
 * Allows admin to view and delete buses from the system.
 */
public class ManageBuses extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton refreshButton, deleteButton;
    private JPanel buttonPanel;

    public ManageBuses() {
        setTitle("Manage Buses");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]
        		{"Bus ID", "Bus Number", "Total Seats", "Start", "End", "Time", "Fare"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        refreshButton = new JButton("Refresh Buses");
        deleteButton = new JButton("Delete Selected Bus");
        buttonPanel = new JPanel();

        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);

        refreshButton.addActionListener(e -> loadBuses());
        deleteButton.addActionListener(e -> deleteSelectedBus());

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadBuses();  // Load when opening
    }

    /**
     * Loads all buses from the database and displays them in the table.
     */
    private void loadBuses() {
        model.setRowCount(0); // Clear table first
        String sql = "SELECT * FROM buses";
        boolean found = false;

        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int busId = rs.getInt("bus_id");
                String busNumber = rs.getString("bus_number");
                int totalSeats = rs.getInt("total_seats");
                String start = rs.getString("starting_point");
                String end = rs.getString("ending_point");
                String time = rs.getString("start_time");
                double fare = rs.getDouble("fare");
                model.addRow(new Object[]{busId, busNumber, totalSeats, start, end, time, fare});
                found = true;
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "No buses found.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading buses: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Deletes the selected bus from the table and database.
     */
    private void deleteSelectedBus() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int busId = (int) model.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this bus?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (deleteBusFromDB(busId)) {
                    JOptionPane.showMessageDialog(this, "Bus deleted successfully.");
                    loadBuses();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete bus.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a bus to delete.");
        }
    }

    /**
     * Deletes a bus from the database by ID.
     *
     * @param busId The ID of the bus to delete.
     * @return true if the bus was deleted; false otherwise.
     */
    private boolean deleteBusFromDB(int busId) {
        String sql = "DELETE FROM buses WHERE bus_id = ?";
        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, busId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageBuses().setVisible(true));
    }
}
