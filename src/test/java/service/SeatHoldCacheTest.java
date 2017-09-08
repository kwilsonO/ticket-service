package service;

import model.SeatHold;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

public class SeatHoldCacheTest {

    SeatService seatService = new SeatService(20);
    SeatHoldCache seatHoldCache = new SeatHoldCache(seatService);

    @Test(expected = Exception.class)
    public void getNonExistentSeatHoldIdTest(){

        try {
            seatHoldCache.cache.get(1l);
            assert(false);
        } catch (NoSuchElementException nse){
            System.out.println(nse.getMessage());
            assert(true);
        } catch (ExecutionException e){
            System.out.println(e.getMessage());
            assert(false);
        }
    }

    @Test
    public void testRetrievalOfExpiredRequest(){

        try {
            SeatHold seatHold1 = seatService.findAndHoldSeats(2, "kwilson");

            seatService.seatDebug();

            SeatHold seatHold2 = seatService.findAndHoldSeats(1, "superduper");

            seatService.seatDebug();

            SeatHold seatHold3 = seatService.findAndHoldSeats(3, "yay");

            seatService.seatDebug();

            seatService.reserveSeat((int)seatHold1.getSeatHoldId());

            seatHoldCache.cache.invalidateAll();

            seatService.findAndHoldSeats(1, "ddd");
            seatService.findAndHoldSeats(1, "ddd");
            seatService.findAndHoldSeats(1, "ddd");
            seatService.seatDebug();

        } catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

}
