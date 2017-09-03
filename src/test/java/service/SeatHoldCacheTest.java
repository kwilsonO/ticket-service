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
            SeatHold seatHold2 = seatService.findAndHoldSeats(1, "superduper");

            seatService.seatDebug();

            Thread.sleep(6000);
            seatHoldCache.cache.cleanUp();
            SeatHold seatHold3 = seatService.findAndHoldSeats(3, "yay");

            seatService.seatDebug();

        } catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

}
