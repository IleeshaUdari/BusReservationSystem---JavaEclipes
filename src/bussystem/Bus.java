package bussystem;

import javax.swing.*;  
import java.sql.*;

public class Bus {
    private String busNumber;
    private int totalSeats;
    private String startPoint;
    private String endPoint;
    private String startTime;
    private double fare;

    public Bus(String busNumber, int totalSeats, String startPoint, String endPoint, String startTime, double fare) {
        this.busNumber = busNumber;
        this.totalSeats = totalSeats;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startTime = startTime;
        this.fare = fare;
    }

    public String getBusNumber() { return busNumber; }
    public int getTotalSeats() { return totalSeats; }
    public String getStartPoint() { return startPoint; }
    public String getEndPoint() { return endPoint; }
    public String getStartTime() { return startTime; }
    public double getFare() { return fare; }

    // Fixed searchBuses method to work properly with ResultSet
    public static ResultSet searchBuses(String start, String end) {
        try {
            // Assuming DatabaseOperations.getConnection() is already set up to return a valid connection
            Connection conn = DatabaseOperations.getConnection();
            String query = "SELECT * FROM buses WHERE starting_point LIKE ? AND ending_point LIKE ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + start + "%");
            ps.setString(2, "%" + end + "%");
            return ps.executeQuery();
        } catch (SQLException e) {
            // Now this works because JOptionPane is imported
            JOptionPane.showMessageDialog(null, "Error searching buses.");
            e.printStackTrace();
        }
        return null;
    }
}
