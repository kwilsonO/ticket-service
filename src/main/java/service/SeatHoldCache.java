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


        CacheLoader<Long, SeatHold> loader = new CacheLoader<Long, SeatHold> () {
            public SeatHold load(Long seatHoldId) throws Exception {
                //this should only be called when attempting to retrieve a SeatHold object
                //that is NOT in the cache. For example if it's removed due to timeout.
                System.out.println("LOADER CALLED: " + seatHoldId);
                return null;
            }
        };

        RemovalListener<Long, SeatHold> removalListener = new RemovalListener<Long, SeatHold>() {
            public void onRemoval(RemovalNotification<Long, SeatHold> removal) {
                System.out.println("REMOVAL CALLED: " + removal.getKey());
                for(Seat s : removal.getValue().getHeldSeats()){
                    seatService.addAvailableSeat(s);
                    cache.invalidate(removal.getKey());
                }
            }
        };

        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .removalListener(removalListener)
                .build(loader);
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

            return true;

        } catch(ExecutionException ee){
           System.out.println("An Execution Exception Occured!" + ee.getMessage());
           return false;
        }
    }
}
