package service;

import com.google.common.cache.*;
import model.Seat;
import model.SeatHold;
import model.SeatStatus;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class SeatHoldCache {

    Cache<Long, SeatHold> cache;
    SeatService seatService;
    private final AtomicLong seatHoldIdCounter = new AtomicLong();

    public SeatHoldCache(SeatService service){

        seatService = service;

        RemovalListener<Long, SeatHold> removalListener = new RemovalListener<Long, SeatHold>() {
            public void onRemoval(RemovalNotification<Long, SeatHold> removal) {
                for(Seat s : removal.getValue().getHeldSeats()){
                    s.setStatus(SeatStatus.AVAILABLE);
                    seatService.addSeat(s);
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
}
