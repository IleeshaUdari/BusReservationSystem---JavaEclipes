package bussystem;

import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseOperations {

    private static final String DB_URL = "jdbc:sqlite:BusSystemDB.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return conn;
    }

    public static void dbInit() throws SQLException {
        createTables();
    }

    public static boolean validateUserLogin(String userId, String password) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE user_id=? AND password=?")) {
            ps.setString(1, userId);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean registerUser(String name, String password, String email, String phone, String city, int age) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO users (name, password, email, phone, city, age) VALUES (?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, city);
            ps.setInt(6, age);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet loadUsers() {
        try {
            Connection conn = getConnection();
            Statement st = conn.createStatement();
            return st.executeQuery("SELECT * FROM users");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet loadBuses() {
        try {
            Connection conn = getConnection();
            Statement st = conn.createStatement();
            return st.executeQuery("SELECT * FROM buses");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean addBus(String busId, String busName, String source, String destination, int totalSeats) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO buses (bus_id, bus_name, source, destination, total_seats) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, busId);
            ps.setString(2, busName);
            ps.setString(3, source);
            ps.setString(4, destination);
            ps.setInt(5, totalSeats);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteBus(String busId) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM buses WHERE bus_id = ?")) {
            ps.setString(1, busId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean reserveSeat(String userId, String busId, int seatNo) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO reservations (user_id, bus_id, seat_no) VALUES (?, ?, ?)")) {
            ps.setString(1, userId);
            ps.setString(2, busId);
            ps.setInt(3, seatNo);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Seat reserved successfully!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean cancelReservationById(int reservationId) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM reservations WHERE reservation_id = ?")) {
            ps.setInt(1, reservationId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet loadReservations() {
        try {
            Connection conn = getConnection();
            Statement st = conn.createStatement();
            return st.executeQuery("SELECT * FROM reservations");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ResultSet getUserReservations(String userId) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM reservations WHERE user_id = ?");
            ps.setString(1, userId);
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean addToWaitlist(String userId, String busId) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO waiting_list (user_id, bus_id) VALUES (?, ?)")) {
            ps.setString(1, userId);
            ps.setString(2, busId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "You have been added to the waiting list.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ResultSet loadWaitlist() {
        try {
            Connection conn = getConnection();
            Statement st = conn.createStatement();
            return st.executeQuery("SELECT * FROM waiting_list");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean removeFromWaitlist(int waitingId) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM waiting_list WHERE waiting_id = ?")) {
            ps.setInt(1, waitingId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void addReservation(int userId, int busId, int seatsBooked, int totalFare, String date) {
        String sql = "INSERT INTO reservations (user_id, bus_id, reserved_seats, total_fare, reservation_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, busId);
            ps.setInt(3, seatsBooked);
            ps.setInt(4, totalFare);
            ps.setString(5, date);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add reservation: " + e.getMessage());
        }
    }

    public static boolean validateUserLogin(int userId, String password) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createTables() {
        createUsersTable();
        createBusTable();
        createReservationsTable();
        createWaitingListTable();
        System.out.println("All tables created or verified.");
    }

    
    public static void createUsersTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                user_id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                password TEXT NOT NULL,
                email TEXT NOT NULL,
                phone TEXT,
                city TEXT,
                age INTEGER
            );
        """;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createBusTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS buses (
                bus_id TEXT PRIMARY KEY,
                bus_name TEXT,
                source TEXT,
                destination TEXT,
                total_seats INTEGER
            );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table 'buses' created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating table 'buses': " + e.getMessage());
        }
    }

    public static void createReservationsTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS reservations (
                reservation_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id TEXT NOT NULL,
                bus_id TEXT NOT NULL,
                seat_no INTEGER NOT NULL,
                FOREIGN KEY (user_id) REFERENCES users(user_id),
                FOREIGN KEY (bus_id) REFERENCES buses(bus_id)
            );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table 'reservations' created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating table 'reservations': " + e.getMessage());
        }
    }

    public static void createWaitingListTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS waiting_list (
                waiting_id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id TEXT NOT NULL,
                bus_id TEXT NOT NULL,
                request_time TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(user_id),
                FOREIGN KEY (bus_id) REFERENCES buses(bus_id)
            );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table 'waiting_list' created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating table 'waiting_list': " + e.getMessage());
        }
    }
}
