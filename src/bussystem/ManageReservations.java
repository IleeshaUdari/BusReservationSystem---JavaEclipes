package bussystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * GUI panel to manage reservations including loading, canceling, searching buses, and reserving seats.
 */
public class ManageReservations extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton loadBtn, cancelBtn, searchBtn, reserveBtn, btnReserve;

    private int userId = 1; // Example hardcoded user ID

    public ManageReservations() {
        setTitle("Manage Reservations");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"Reservation ID", "User ID", "Bus ID", "Seat Number"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel();
        loadBtn = new JButton("Load Reservations");
        cancelBtn = new JButton("Cancel Reservation");
        searchBtn = new JButton("Search Buses");
        reserveBtn = new JButton("Reserve Seat");
        btnReserve = new JButton("Make Reservation");

        buttonPanel.add(loadBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(reserveBtn);
        buttonPanel.add(btnReserve);

        loadBtn.addActionListener(e -> loadReservations());
        cancelBtn.addActionListener(e -> cancelReservation());
        searchBtn.addActionListener(e -> searchBuses());
        reserveBtn.addActionListener(e -> reserveSeat());

        /** Opens Reservation Panel for manual booking */
        btnReserve.addActionListener(e -> new ReservationPanel(userId).setVisible(true));

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadReservations(); // Initial load
    }

    /**
     * Loads all reservations from the database and displays them in the table.
     */
    private void loadReservations() {
        model.setRowCount(0); // Clear the table first
        String sql = "SELECT * FROM reservations";
        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;
            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                int userId = rs.getInt("user_id");
                int busId = rs.getInt("bus_id");
                int seatNumber = rs.getInt("reserved_seats");
                model.addRow(new Object[]{reservationId, userId, busId, seatNumber});
                found = true;
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "No reservations found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading reservations: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Cancels the selected reservation in the table.
     */
    private void cancelReservation() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to cancel.");
            return;
        }

        int reservationId = (int) model.getValueAt(row, 0);
        try {
            if (DatabaseOperations.cancelReservationById(reservationId)) {
                JOptionPane.showMessageDialog(this, "Reservation cancelled successfully.");
                loadReservations(); // Refresh table
            } else {
                JOptionPane.showMessageDialog(this, "Cancellation failed.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cancelling reservation: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Searches for buses by start and end points.
     */
    private void searchBuses() {
        String start = JOptionPane.showInputDialog("Enter Start Point:");
        String end = JOptionPane.showInputDialog("Enter End Point:");

        String sql = "SELECT * FROM buses WHERE starting_point = ? AND ending_point = ?";
        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, start);
            ps.setString(2, end);
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    String busNumber = rs.getString("bus_number");
                    String startPoint = rs.getString("starting_point");
                    String endPoint = rs.getString("ending_point");
                    String startTime = rs.getString("start_time");
                    double fare = rs.getDouble("fare");

                    JOptionPane.showMessageDialog(this,
                            "Bus Number: " + busNumber + "\nStart: " + startPoint + "\nEnd: " + endPoint +
                                    "\nTime: " + startTime + "\nFare: " + fare);
                    found = true;
                }
                if (!found) {
                    JOptionPane.showMessageDialog(this, "No buses found for the given route.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error searching buses: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Reserves a seat for a user on a specific bus.
     */
    private void reserveSeat() {
        try {
            int seatNumber = Integer.parseInt(JOptionPane.showInputDialog("Enter Seat Number to Reserve:"));
            int busId = Integer.parseInt(JOptionPane.showInputDialog("Enter Bus ID:"));
            int totalFare = seatNumber * 500; // Example calculation
            String date = JOptionPane.showInputDialog("Enter Reservation Date (YYYY-MM-DD):");

            DatabaseOperations.addReservation(userId, busId, seatNumber, totalFare, date);
            JOptionPane.showMessageDialog(this, "Seat reserved successfully.");
            loadReservations(); // Refresh
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Reservation failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Launches the reservation management panel.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageReservations().setVisible(true));
    }
}
