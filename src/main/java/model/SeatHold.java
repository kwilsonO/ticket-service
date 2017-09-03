package model;

import java.util.ArrayList;

public class SeatHold {

    private long seatHoldId;
    private String customerEmail;
    private ArrayList<Seat> heldSeats;

    public SeatHold(long seatHoldId, String customerEmail, ArrayList<Seat> heldSeats){
        this.seatHoldId = seatHoldId;
        this.customerEmail = customerEmail;
        this.heldSeats = heldSeats;
    }

    public long getSeatHoldId(){
        return this.seatHoldId;
    }

    public ArrayList<Seat> getHeldSeats(){
        return this.heldSeats;
    }

}
