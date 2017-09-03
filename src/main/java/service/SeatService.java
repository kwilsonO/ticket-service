package service;

import model.Seat;
import model.SeatHold;
import model.SeatStatus;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

public class SeatService {

    ArrayList<Seat> reservedSeats;
    PriorityQueue<Seat> availableSeats;
    SeatHoldCache seatHoldCache;
    AtomicLong seatHoldCounter = new AtomicLong();

    public SeatService(int totalSeats){
        availableSeats = new PriorityQueue<Seat>(new SeatComparator());
        fillAvailableSeats(totalSeats);
        reservedSeats = new ArrayList<Seat>();
        seatHoldCache = new SeatHoldCache(this);
    }

    public void fillAvailableSeats(int totalSeats){

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
       s.setStatus(SeatStatus.AVAILABLE);
       availableSeats.offer(s);
    }

    public void addReservedSeat(Seat s){
        s.setStatus(SeatStatus.RESERVED);
        reservedSeats.add(s);
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

    public boolean reserveSeat(int seatHoldId){

        return seatHoldCache.removeAndReserveSeats(seatHoldId);
    }

    public void seatDebug(){
        System.out.println("Reserved Seats:\n");
        for(Seat s : this.reservedSeats){
            System.out.println(s.getSeatNumber() + " " + s.getStatus());
        }

        System.out.println("Available seats: " + this.availableSeats.size() +"\n");

        System.out.println("Held Seats: " + this.seatHoldCache.cache.size() + "\n");
    }
}
