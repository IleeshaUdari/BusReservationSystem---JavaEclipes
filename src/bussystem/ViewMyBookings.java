/**
 * This class displays all bookings made by the currently logged-in user.
 */
package bussystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewMyBookings extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private int userId;

    /**
     * Constructor initializes the ViewMyBookings GUI for a specific user.
     * @param userId The ID of the logged-in user.
     */
    public ViewMyBookings(int userId) {
        this.userId = userId;

        setTitle("My Bookings");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"Reservation ID", "Bus ID", "Seat Number"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        loadBookings();
    }

    /**
     * Loads booking data for the current user from the database.
     */
    private void loadBookings() {
        model.setRowCount(0);
        String sql = "SELECT reservation_id, bus_id, seat_number FROM reservations WHERE user_id = ?";

        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                int busId = rs.getInt("bus_id");
                int seatNumber = rs.getInt("seat_number");
                model.addRow(new Object[]{reservationId, busId, seatNumber});
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading bookings.");
            ex.printStackTrace();
        }
    }

    /**
     * Entry point for testing ViewMyBookings independently.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewMyBookings(1).setVisible(true)); // Pass test userId
    }
}
