package service;

import model.Seat;
import model.SeatHold;
import org.junit.Test;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SeatServiceTest {

    private static Logger logger = Logger.getLogger("SeatServiceTest");
    SeatService seatService;


    @Test
    public void getBestAvailableSeatsTest(){

        seatService = new SeatService();
        seatService.init(500);

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

        seatService = new SeatService();
        seatService.init(500);

        assert(seatService.getNumAvailableSeats() == 500);

        SeatHold seatHold = seatService.findAndHoldSeats(25, "kylewilson52@gmail.com");
        assert(seatHold.getHeldSeats().size() == 25);

        assert(seatService.getNumAvailableSeats() == 475);

        SeatHold seatHold1 = seatService.findAndHoldSeats(1, "testemail");

        assert(seatService.getNumAvailableSeats() == 474);
        assert(seatHold1.getHeldSeats().size() == 1);
    }

    @Test
    public void testReserveSeat() {

       seatService = new SeatService();
       seatService.init(500);

       assert(seatService.getNumAvailableSeats() == 500);

       SeatHold seatHold = seatService.findAndHoldSeats(100, "testemail");

       seatService.reserveSeat(seatHold.getSeatHoldId());

       assert(seatService.availableSeats.size() == 400);
       assert(seatService.reservedSeats.size() == 100);

       //next best available seat should be seat 101
       assert(seatService.availableSeats.peek().getSeatNumber() == 101);

    }

}
