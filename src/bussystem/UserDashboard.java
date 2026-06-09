package bussystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * Represents the main user dashboard for bus reservations.
 */
public class UserDashboard extends JFrame {
    private int userId;

    /**
     * Constructor for UserDashboard
     * @param userId The ID of the logged-in user
     */
    public UserDashboard(int userId) {
        this.userId = userId;

        setTitle("User Dashboard");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));

        JButton btnReserve = new JButton("Make Reservation");
        JButton viewBookingsBtn = new JButton("View My Bookings");
        JButton cancelReservationBtn = new JButton("Cancel Reservation");
        JButton searchBusesBtn = new JButton("Search Buses");

        panel.add(btnReserve);
        panel.add(viewBookingsBtn);
        panel.add(cancelReservationBtn);
        panel.add(searchBusesBtn);

        btnReserve.addActionListener(e -> new ReservationPanel(userId).setVisible(true));
        viewBookingsBtn.addActionListener(e -> openViewMyBookings());
        cancelReservationBtn.addActionListener(e -> openCancelReservation());
        searchBusesBtn.addActionListener(e -> searchBuses());

        add(panel);
    }

    /**
     * Opens the reservation booking panel.
     */
    private void openReservationPanel() {
        ReservationPanel reservationPanel = new ReservationPanel(userId);
        reservationPanel.setVisible(true);
    }

    /**
     * Opens the panel to view user's bookings.
     */
    private void openViewMyBookings() {
        ViewMyBookings viewBookings = new ViewMyBookings(userId);
        viewBookings.setVisible(true);
    }

    /**
     * Opens the panel to cancel a reservation.
     */
    private void openCancelReservation() {
        CancelReservationPanel cancelPanel = new CancelReservationPanel(userId);
        cancelPanel.setVisible(true);
    }

    /**
     * Allows users to search for buses by start and end points.
     */
    private void searchBuses() {
        String start = JOptionPane.showInputDialog("Enter Start Point:");
        String end = JOptionPane.showInputDialog("Enter End Point:");

        try {
            ResultSet rs = Bus.searchBuses(start, end); // Assuming Bus.searchBuses is properly implemented
            if (rs != null) {
                StringBuilder resultText = new StringBuilder("Available Buses:\n");
                while (rs.next()) {
                    String busNumber = rs.getString("bus_number");
                    String startPoint = rs.getString("starting_point");
                    String endPoint = rs.getString("ending_point");
                    double fare = rs.getDouble("fare");

                    resultText.append("Bus Number: ").append(busNumber)
                            .append(", Start: ").append(startPoint)
                            .append(", End: ").append(endPoint)
                            .append(", Fare: ").append(fare)
                            .append("\n");
                }
                JOptionPane.showMessageDialog(this, resultText.toString());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error displaying buses.");
            ex.printStackTrace();
        }
    }

    /**
     * Cancels a reservation and handles waitlist updates.
     * @param reservationId The reservation to cancel
     * @return true if successfully canceled, else false
     */
    public static boolean cancelReservationById(int reservationId) {
        String query = "DELETE FROM reservations WHERE reservation_id = ?";

        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, reservationId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Reservation canceled successfully!");
                handleWaitlistForSeat(reservationId);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Placeholder: Handle waitlist updates when a reservation is canceled.
     * @param reservationId The canceled reservation ID
     */
    private static void handleWaitlistForSeat(int reservationId) {
        // To be implemented: Notify next person in waitlist
    }

    /**
     * Adds a user to the waitlist for a bus seat.
     * @param userId The ID of the user
     * @param busId The ID of the bus
     * @param seatNumber The desired seat number
     * @return true if added successfully
     */
    public static boolean addToWaitlist(int userId, int busId, int seatNumber) {
        String query = "INSERT INTO waitlist (user_id, bus_id, seat_number) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseOperations.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setInt(2, busId);
            ps.setInt(3, seatNumber);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "You have been added to the waitlist.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Test method to run UserDashboard independently.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserDashboard(1).setVisible(true));
    }
}
