package service;

import model.SeatHold;

public class TicketServiceManager implements TicketService {

    SeatService seatService = new SeatService(500);

    public int numSeatsAvailable() {
        return seatService.getNumAvailableSeats();
    }

    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
        return null;
    }

    public String reserveSeats(int seatHoldId, String customerEmail) {
        return null;
    }
}
