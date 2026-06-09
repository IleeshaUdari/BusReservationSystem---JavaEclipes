package bussystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReservationPanel extends JFrame {
    private int userId;
    private JComboBox<String> busComboBox;
    private JTextField seatNumberField;
    private Map<String, Integer> busMap = new HashMap<>();

    public ReservationPanel(int userId) {
        this.userId = userId;
        setTitle("Reservation Panel");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1, 10, 10));

        busComboBox = new JComboBox<>();
        seatNumberField = new JTextField();
        JButton reserveButton = new JButton("Reserve Seat");

        loadBuses();

        add(new JLabel("Select Bus:"));
        add(busComboBox);
        add(new JLabel("Seat Number:"));
        add(seatNumberField);
        add(reserveButton);

        reserveButton.addActionListener(e -> reserveSeat());

        setVisible(true);
    }

    private void loadBuses() {
        String sql = "SELECT * FROM buses";
        try (
            Connection conn = DatabaseOperations.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int busId = rs.getInt("bus_id");
                String busDisplay = rs.getString("bus_number") + " (" + rs.getString("source") + " → " + rs.getString("destination") + ")";
                busComboBox.addItem(busDisplay);
                busMap.put(busDisplay, busId);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading buses: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void reserveSeat() {
        try {
            String selectedBus = (String) busComboBox.getSelectedItem();
            if (selectedBus == null || selectedBus.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a bus.");
                return;
            }

            int busId = busMap.get(selectedBus);
            int seatNumber = Integer.parseInt(seatNumberField.getText().trim());
            String date = LocalDate.now().toString();
            int fare = 500;

            if (isSeatAlreadyBooked(busId, seatNumber, date)) {
                JOptionPane.showMessageDialog(this, "Seat already booked. Please choose another.");
                return;
            }

            DatabaseOperations.addReservation(userId, busId, seatNumber, fare, date);
            JOptionPane.showMessageDialog(this, "Reservation successful!");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid seat number.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error reserving seat: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean isSeatAlreadyBooked(int busId, int seatNumber, String date) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations WHERE bus_id = ? AND seat_number = ? AND date = ?";
        try (
            Connection conn = DatabaseOperations.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, busId);
            ps.setInt(2, seatNumber);
            ps.setString(3, date);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}
