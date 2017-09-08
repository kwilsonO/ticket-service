package service;

import model.Seat;
import model.SeatHold;
import org.junit.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SeatServiceTest {

    private static Logger logger = Logger.getLogger("SeatServiceTest");
    SeatService seatService = new SeatService();


    @Test
    public void getBestAvailableSeatsTest(){

        assert(seatService.getNumAvailableSeats() == 500);
        ArrayList<Seat> seats = seatService.getBestAvailableSeats(100);


        //should be the first 100 seats
        int seatNumber = 1;
        for(Seat s : seats){
            assert(s.getSeatNumber() == seatNumber++);
        }

        assert(seatService.getNumAvailableSeats() == 400);

        for(Seat s : seats){
            seatService.addAvailableSeat(s);
        }

        assert(seatService.getNumAvailableSeats() == 500);
    }

    @Test
    public void findAndHoldTest(){

        assert(seatService.getNumAvailableSeats() == 500);

        SeatHold seatHold = seatService.findAndHoldSeats(25, "kylewilson52@gmail.com");

        assert(seatService.getNumAvailableSeats() == 475);

        assert(seatService.reserveSeat(seatHold.getSeatHoldId()));

        assert(seatService.getNumReservedSeats() == 25);

        assert(seatService.getNumAvailableSeats() == 475);
    }

    @Test
    public void testHoldRequestExpired(){

        try {

            seatService.seatDebug();
            SeatHold seatHold = seatService.findAndHoldSeats(10, "superduper");
            seatService.seatDebug();
            //Thread.sleep(5000);
            SeatHold seatHold1 = seatService.findAndHoldSeats(25, "yay");
            seatService.seatDebug();
            seatService.seatDebug();
        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }
}
