package service;

import model.Seat;
import model.SeatHold;
import org.junit.Test;

import java.util.ArrayList;

public class SeatServiceTest {

    SeatService seatService = new SeatService(500);


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

        assert(seatService.reserveSeat((int)seatHold.getSeatHoldId()));

        assert(seatService.getNumReservedSeats() == 25);

        assert(seatService.getNumAvailableSeats() == 475);
    }
}
