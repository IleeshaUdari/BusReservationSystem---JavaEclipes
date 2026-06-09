package bussystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CancelReservationPanel extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton btnCancelReservation;
    private int userId;

    public CancelReservationPanel(int userId) {
        this.userId = userId;

        setTitle("Cancel Reservation");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"Reservation ID", "Bus ID", "Seat Number"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel();
        btnCancelReservation = new JButton("Cancel Selected Reservation");
        btnCancelReservation.addActionListener(e -> cancelReservation());
        buttonPanel.add(btnCancelReservation);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadUserReservations();
    }

    private void loadUserReservations() {
        model.setRowCount(0);
        String sql = "SELECT reservation_id, bus_id, seat_no FROM reservations WHERE user_id = ?";
        
        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                String busId = rs.getString("bus_id");
                int seatNo = rs.getInt("seat_no");

                model.addRow(new Object[]{reservationId, busId, seatNo});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading reservations.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void cancelReservation() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to cancel.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int reservationId = (int) model.getValueAt(selectedRow, 0);

        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        
        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Reservation cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserReservations();
            } else {
                JOptionPane.showMessageDialog(this, "Cancellation failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error cancelling reservation.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
