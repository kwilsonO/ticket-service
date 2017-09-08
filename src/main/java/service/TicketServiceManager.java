package service;

import model.SeatHold;

public class TicketServiceManager implements TicketService {

    SeatService seatService;

    public TicketServiceManager(int numSeats){
        seatService = new SeatService();
        seatService.init(numSeats);
    }

    public int numSeatsAvailable() {
        return seatService.getNumAvailableSeats();
    }

    public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {

        //maybe needs to be changed the interface spec doesn't specify how to handle
        //non-positive seat counts
        if(numSeats <= 0){
            return null;
        }

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
