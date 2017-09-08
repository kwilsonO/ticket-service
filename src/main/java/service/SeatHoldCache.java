package service;

import com.google.common.cache.*;
import model.Seat;
import model.SeatHold;
import model.SeatStatus;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SeatHoldCache {

    Cache<Long, SeatHold> cache;
    SeatService seatService;
    private final AtomicLong seatHoldIdCounter = new AtomicLong();

    public SeatHoldCache(SeatService service){

        seatService = service;

        CacheBuilder builder=CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.SECONDS).removalListener(
                new RemovalListener(){
                    {
                        System.out.println("Removal Listener created");
                    }
                    public void onRemoval(RemovalNotification notification) {
                        System.out.println("Going to remove data from InputDataPool");
                        System.out.println("Following data is being removed:"+notification.getKey());
                        if(notification.getCause()==RemovalCause.EXPIRED)
                        {
                            System.out.println("This data expired:"+notification.getKey());
                        }else
                        {
                            System.out.println("This data didn't expired but evacuated intentionally"+notification.getKey());
                        }

                    }}
        );

        cache = builder.build(new CacheLoader() {
            @Override
            public Object load(Object o) throws Exception {
                //this should only be called when attempting to retrieve a SeatHold object
                //that is NOT in the cache. For example if it's removed due to timeout.
                System.out.println("LOADER CALLED: ");
                return null;
            }
        });
    }

    public void holdSeats(SeatHold seatHold){
        cache.asMap().put(seatHold.getSeatHoldId(), seatHold);
    }

    public boolean removeAndReserveSeats(long seatHoldId){

        try {
            SeatHold seatHold = cache.get(seatHoldId);

            if(seatHold == null){
                System.out.println("Cannot reserve seat due to request being expired!");
                return false;
            }

            for(Seat s : seatHold.getHeldSeats()){
                seatService.addReservedSeat(s);
            }

            cache.invalidate(new Long(seatHoldId));
            return true;

        } catch(ExecutionException ee){
           System.out.println("An Execution Exception Occured!" + ee.getMessage());
           return false;
        }
    }
}
