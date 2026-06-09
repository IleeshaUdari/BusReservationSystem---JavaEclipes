package bussystem;

public class WaitlistEntry {
    private int userId;
    private String busNumber;

    public WaitlistEntry(int userId, String busNumber) {
        this.userId = userId;
        this.busNumber = busNumber;
    }

    public int getUserId() { return userId; }
    public String getBusNumber() { return busNumber; }
}
