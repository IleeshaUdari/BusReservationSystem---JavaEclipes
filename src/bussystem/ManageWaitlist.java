package bussystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageWaitlist extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton loadBtn, removeBtn;
    private JPanel buttonPanel;

    public ManageWaitlist() {
        setTitle("Manage Waitlist");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"Waitlist ID", "User ID", "Bus ID", "Seat Number"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        loadBtn = new JButton("Load Waiting List");
        removeBtn = new JButton("Remove from List");
        buttonPanel = new JPanel();

        buttonPanel.add(loadBtn);
        buttonPanel.add(removeBtn);

        loadBtn.addActionListener(e -> loadWaitlist());
        removeBtn.addActionListener(e -> removeWaitingListEntry());

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadWaitlist();  // Initial loading when page opens
    }

    private void loadWaitlist() {
        model.setRowCount(0); // Clear previous data
        try {
            ResultSet rs = DatabaseOperations.loadWaitlist();
            boolean found = false;
            while (rs != null && rs.next()) {
                int waitlistId = rs.getInt("waitlist_id");
                int userId = rs.getInt("user_id");
                int busId = rs.getInt("bus_id");
                int seatNumber = rs.getInt("seat_number");
                model.addRow(new Object[]{waitlistId, userId, busId, seatNumber});
                found = true;
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "No waitlist entries found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading waitlist: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void removeWaitingListEntry() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an entry to remove.");
            return;
        }

        int waitlistId = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this waitlist entry?", "Confirm Remove", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = DatabaseOperations.removeFromWaitlist(waitlistId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Waitlist entry removed successfully.");
                loadWaitlist();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove waitlist entry.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageWaitlist().setVisible(true));
    }
    
    
}
