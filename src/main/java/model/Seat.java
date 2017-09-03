package model;

public class Seat {

    private int seatNumber;
    private SeatStatus status;

    public Seat(int seatNumber){
        this.seatNumber = seatNumber;
        this.status = SeatStatus.AVAILABLE;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }
}
