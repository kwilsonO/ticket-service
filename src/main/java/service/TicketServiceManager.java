package service;

import model.SeatHold;

public class TicketServiceManager implements TicketService {

    SeatService seatService = new SeatService(500);

    public int numSeatsAvailable() {
        return seatService.getNumAvailableSeats();
    }

    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        return seatService.findAndHoldSeats(numSeats, customerEmail);
    }

    public String reserveSeats(int seatHoldId, String customerEmail) {

        if(seatService.reserveSeat(seatHoldId)){
           return "Reservation Code " + customerEmail + " " + seatHoldId;
        } else {
           //seats could not be reserved, most likely due to expiry
           return "Could not reserve seats!";
        }
    }
}
