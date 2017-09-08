package service;

import model.SeatHold;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class SeatHoldCacheTest {

    private static Logger logger = Logger.getLogger("SeatHoldCacheTest");
    SeatService seatService = new SeatService(20);

    @Test(expected = Exception.class)
    public void getNonExistentSeatHoldIdTest(){

        try {
            seatService.seatHoldCache.cache.get(1);
            assert(false);
        } catch (NoSuchElementException nse){
            logger.info(nse.getMessage());
            assert(true);
        } catch (ExecutionException e){
            logger.info(e.getMessage());
            assert(false);
        }
    }

    @Test
    public void testRetrievalOfExpiredRequest(){

        try {

        } catch(Exception e){
            logger.info(e.getMessage());
        }

    }

}
