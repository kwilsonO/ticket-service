package service;

import model.SeatHold;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class SeatHoldCacheTest {

    private static Logger logger = Logger.getLogger("SeatHoldCacheTest");

    @Test(expected = Exception.class)
    public void getNonExistentSeatHoldIdTest(){
    }

    @Test
    public void testRetrievalOfExpiredRequest(){

        try {

        } catch(Exception e){
            logger.info(e.getMessage());
        }

    }

}
