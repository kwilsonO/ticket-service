package model;

import java.util.ArrayList;

public class SeatHold {

    private Integer seatHoldId;
    private String customerEmail;
    private ArrayList<Seat> heldSeats;

    public SeatHold(Integer seatHoldId, String customerEmail, ArrayList<Seat> heldSeats){
        this.seatHoldId = seatHoldId;
        this.customerEmail = customerEmail;
        this.heldSeats = heldSeats;
    }

    public Integer getSeatHoldId(){
        return this.seatHoldId;
    }

    public ArrayList<Seat> getHeldSeats(){
        return this.heldSeats;
    }

}
