package bussystem;

public class Reservation {
    private int userId;
    private String busNumber;
    private int seatNumber;

    public Reservation(int userId, String busNumber, int seatNumber) {
        this.userId = userId;
        this.busNumber = busNumber;
        this.seatNumber = seatNumber;
    }

    public int getUserId() { return userId; }
    public String getBusNumber() { return busNumber; }
    public int getSeatNumber() { return seatNumber; }
}
