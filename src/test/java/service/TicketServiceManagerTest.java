package service;

import model.SeatHold;
import org.junit.Test;

public class TicketServiceManagerTest {

    TicketServiceManager ticketServiceManager;

    @Test
    public void testNumSeatsAvailable(){

        ticketServiceManager = new TicketServiceManager(500);

        assert(ticketServiceManager.numSeatsAvailable() == 500);

        ticketServiceManager = new TicketServiceManager(400);

        assert(ticketServiceManager.numSeatsAvailable() == 400);

    }

    @Test
    public void testFindAndHoldSeats(){

        ticketServiceManager = new TicketServiceManager(500);

        SeatHold seatHold = ticketServiceManager.findAndHoldSeats(100, "requestfor100seats");

        assert(ticketServiceManager.numSeatsAvailable() == 400);
        assert(seatHold.getHeldSeats().size() == 100);

        //should return null
        seatHold = ticketServiceManager.findAndHoldSeats(0, "request 0 seats");

        assert(ticketServiceManager.numSeatsAvailable() == 400);
        assert(seatHold == null);

        seatHold = ticketServiceManager.findAndHoldSeats(400, "requestfor400seats");

        assert(ticketServiceManager.numSeatsAvailable() == 0);
        assert(seatHold.getHeldSeats().size() == 400);
    }

    @Test
    public void testReserveSeats(){

        ticketServiceManager = new TicketServiceManager(500);

        SeatHold seatHold = ticketServiceManager.findAndHoldSeats(100, "superb");

        assert(seatHold.getHeldSeats().size() == 100);
        assert(ticketServiceManager.numSeatsAvailable() == 400);

        ticketServiceManager.reserveSeats(seatHold.getSeatHoldId(), "superb");

        assert(ticketServiceManager.numSeatsAvailable() == 400);

    }
}
