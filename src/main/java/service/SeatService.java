package service;

import model.Seat;
import model.SeatHold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class SeatService {

    List<Seat> reservedSeats;
    PriorityBlockingQueue<Seat> availableSeats;
    SeatHoldCache seatHoldCache;
    AtomicInteger seatHoldCounter = new AtomicInteger();
    private static Logger logger = Logger.getLogger("SeatService");

    public void init(int numSeats){
        availableSeats = new PriorityBlockingQueue<Seat>(numSeats, new SeatComparator());
        reservedSeats = Collections.synchronizedList(new ArrayList<Seat>());
        fillAvailableSeats(numSeats);
        seatHoldCache = new SeatHoldCache(this);
    }

    private void fillAvailableSeats(int totalSeats){

        for(int i = 1; i <= totalSeats; i++){
            availableSeats.add(
                    new Seat(i)
            );
        }
    }

    public int getNumReservedSeats(){
        return reservedSeats.size();
    }

    public int getNumAvailableSeats(){
        return availableSeats.size();
    }

    public void addAvailableSeat(Seat s){
       availableSeats.offer(s);
    }

    public void addReservedSeat(Seat s){
        reservedSeats.add(s);
    }

    public ArrayList<Seat> getBestAvailableSeats(int numSeats){
        ArrayList<Seat> seats = new ArrayList<>(numSeats);

        //this should be removable with high throughput
        seatHoldCache.refreshSeats();

        //this first condition is actually very important
        //since we are using a thread safe blocking queue,
        //if we attempt to poll the PQ and it's empty it'll
        //block until there is data. This will be only rare
        //cases but there should be a check added somewhere
        //which checks if all seats have been reserved and if
        //so will stop threads from waiting.
        while(availableSeats.size() > 0 && numSeats-- > 0){
            seats.add(availableSeats.poll());
        }

        return seats;
    }

    public SeatHold findAndHoldSeats(int numSeats, String customerEmail){

        SeatHold seatHold = new SeatHold(
                seatHoldCounter.incrementAndGet(),
                customerEmail,
                getBestAvailableSeats(numSeats)
        );

        logger.info("SeatHold #" + seatHold.getSeatHoldId());
        for(Seat s : seatHold.getHeldSeats()){
            logger.info(s.getSeatNumber() + "");
        }

        seatHoldCache.holdSeats(seatHold);

        return seatHold;
    }

    public boolean reserveSeat(int seatHoldId){

        return seatHoldCache.removeAndReserveSeats(seatHoldId);
    }

    public void seatDebug(){
        logger.info("Reserved Seats:\n");
        for(Seat s : this.reservedSeats){
            logger.info(s.getSeatNumber() + "");
        }

        logger.info("Available seats: " + this.availableSeats.size() +"\n");

        logger.info("HoldSeat requests: " + this.seatHoldCache.cache.size() + "\n");
    }
}
