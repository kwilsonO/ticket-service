package service;

import com.google.common.cache.*;
import model.Seat;
import model.SeatHold;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SeatHoldCache {

    Cache<Integer, SeatHold> cache;
    SeatService seatService;
    private static Logger logger = Logger.getLogger("SeatHoldCache");


    public SeatHoldCache(SeatService service){
        seatService = service;

        CacheBuilder builder=CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS).removalListener(
                new RemovalListener(){
                    public void onRemoval(RemovalNotification notification) {
                        SeatHold seatHold = (SeatHold) notification.getValue();
                        if(notification.getCause()==RemovalCause.EXPIRED)
                        {
                            logger.info("This data expired:"+notification.getKey());

                            logger.info("Returning " + seatHold.getHeldSeats().size() + " to available seats");

                            for(Seat s : seatHold.getHeldSeats()){
                                seatService.addAvailableSeat(s);
                            }

                        }else
                        {
                            logger.info("This seat hold req was confirmed "+notification.getKey());
                            for(Seat s : seatHold.getHeldSeats()){
                                seatService.addReservedSeat(s);
                            }

                        }

                    }}
        );

        cache = builder.build(new CacheLoader() {
            @Override
            public Object load(Object o) throws Exception {
                //this should only be called when attempting to retrieve a SeatHold object
                //that is NOT in the cache. For example if it's removed due to timeout.
                logger.info("LOADER CALLED: ");
                return null;
            }
        });
    }

    public void holdSeats(SeatHold seatHold){
        cache.asMap().put(seatHold.getSeatHoldId(), seatHold);
    }

    public boolean removeAndReserveSeats(Integer seatHoldId){

        try {
            SeatHold seatHold = cache.get(seatHoldId);

            if(seatHold == null){
                logger.info("Cannot reserve seat due to request being expired or non-existent!");
                return false;
            }

            cache.invalidate(seatHoldId);
            return true;

        } catch(ExecutionException ee){
           logger.info("An Execution Exception Occured!" + ee.getMessage());
           return false;
        }
    }

    public void refreshSeats(){
        this.cache.cleanUp();
    }
}
