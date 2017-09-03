package service;

import model.Seat;
import model.SeatHold;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

public class SeatService {

    PriorityQueue<Seat> availableSeats;
    SeatHoldCache seatHoldCache;
    AtomicLong seatHoldCounter = new AtomicLong();

    public SeatService(int totalSeats){
        availableSeats = new PriorityQueue<Seat>(new SeatComparator());
        fillAvailableSeats(totalSeats);
        seatHoldCache = new SeatHoldCache(this);
    }

    public void fillAvailableSeats(int totalSeats){

        for(int i = 1; i <= totalSeats; i++){
            availableSeats.add(
                    new Seat(i)
            );
        }
    }

    public int getNumAvailableSeats(){
        return availableSeats.size();
    }

    public void addSeat(Seat s){
       availableSeats.offer(s);
    }

    public ArrayList<Seat> getBestAvailableSeats(int numSeats){
        ArrayList<Seat> seats = new ArrayList<>(numSeats);

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
}
