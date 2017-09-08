package service;

import model.Seat;
import model.SeatHold;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class SeatService {

    ArrayList<Seat> reservedSeats;
    PriorityQueue<Seat> availableSeats;
    SeatHoldCache seatHoldCache;
    AtomicInteger seatHoldCounter = new AtomicInteger();
    private static Logger logger = Logger.getLogger("SeatService");

    public void init(int numSeats){
        availableSeats = new PriorityQueue<>(new SeatComparator());
        reservedSeats = new ArrayList<>();
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

        while(numSeats-- > 0 && availableSeats.size() > 0){
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
