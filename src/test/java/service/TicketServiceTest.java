package service;

import model.SeatHold;
import org.junit.BeforeClass;
import org.junit.Test;

public class TicketServiceTest {

    TicketServiceManager ticketServiceManager = new TicketServiceManager(500);

    @Test
    public void testTicketService(){

       assert(ticketServiceManager.numSeatsAvailable() == 500);

       SeatHold seatHold = ticketServiceManager.findAndHoldSeats(10, "kylewilson52@gmail.com");

       assert(seatHold.getHeldSeats() != null);
       assert(seatHold.getHeldSeats().size() == 10);

       assert(ticketServiceManager.numSeatsAvailable() == 490);

       ticketServiceManager.reserveSeats((int)seatHold.getSeatHoldId(), "kylewilson52@gmail.com");

       assert(ticketServiceManager.numSeatsAvailable() == 490);
    }
}
