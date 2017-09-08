package service;


import model.SeatHold;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ConcurrentSeatServiceTest {

    TicketServiceManager ticketServiceManager;

    @BeforeClass
    public void setUp(){
        ticketServiceManager = new TicketServiceManager(500);
    }


    @Test(threadPoolSize = 4, invocationCount = 10, timeOut = 5000)
    public void basicConcurrencyTest(){
        SeatHold seatHold = ticketServiceManager.findAndHoldSeats(2, "superrrrr");

        ticketServiceManager.reserveSeats(seatHold.getSeatHoldId(), "yay");

    }

}
