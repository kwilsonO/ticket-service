package service;

import model.Seat;

import java.util.Comparator;

public class SeatComparator implements Comparator<Seat> {

    @Override
    public int compare(Seat s1, Seat s2){
        return Integer.compare(s1.getSeatNumber(), s2.getSeatNumber());
    }
}
