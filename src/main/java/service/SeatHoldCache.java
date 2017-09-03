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
                throw new NoSuchElementException("The SeatHoldRequest with id: " + seatHoldId + " was removed due to expiry.");
            }
        };

        RemovalListener<Long, SeatHold> removalListener = new RemovalListener<Long, SeatHold>() {
            public void onRemoval(RemovalNotification<Long, SeatHold> removal) {
                for(Seat s : removal.getValue().getHeldSeats()){
                    s.setStatus(SeatStatus.AVAILABLE);
                    seatService.addAvailableSeat(s);
                }
            }
        };

        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .removalListener(removalListener)
                .build(loader);
    }

    public void holdSeats(SeatHold seatHold){
        cache.asMap().put(seatHold.getSeatHoldId(), seatHold);
    }

    public boolean removeAndReserveSeats(long seatHoldId){

        try {
            SeatHold seatHold = cache.get(seatHoldId);
            for(Seat s : seatHold.getHeldSeats()){
                s.setStatus(SeatStatus.RESERVED);
                seatService.addReservedSeat(s);
            }

            return true;

        } catch(ExecutionException ee){
           System.out.println("An Execution Exception Occured!" + ee.getMessage());
           return false;
        }
    }
}
