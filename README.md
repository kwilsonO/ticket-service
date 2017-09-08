# ticket-service

## Notes/Thoughts:

For the seat layout/storage, I went for a simple approach. Lower seat number means better seat. So depending
on how one chooses to define what a "better" seat is, the seats could be re-mapped without
changing the logic. Lastly, if more complicated logic was needed to pick the 'best' seat, it would be a 
quick change within the SeatComparator.


For this problem, I really wanted a thread safe solution to this problem due to
the project stating it is for a "high-demand" venue, which I envisioned as meaning
many concurrent interactions with my "ticket service".

Taking advantage of this fact I opted to use the Guava CacheBuilder with the expireAfterWrite(invalidates data after I set time), when used with high
throughput(read: often read/writes), it will evict 'expired' data during read/writes meaning there is very little 
overhead compared to running a seperate thread to keep 'cleaning up' expired SeatHold requests. This was key because the cache also supports removal
notifications allowing me to add expired HoldRequest seats back to the available queue, and intentionally removed
requests to the reserved seats list. Also, the cache is thread safe.

For keeping track of available seats, I opted for a thread safe PriorityQueue, to greatly simplify getting 
the next N best available seats, as well as adding held seats back to the available queue if the request expires.

Then for keeping track of already reserved seats, its a simple synchronized list.


## Important:

The refreshSeats() call within getBestAvailableSeats is only there to simplify testing, in a real world environment, 
it should be able to be removed completely, and let the Guava cache handle evicting expired 
requests.


## TODO:

* Test coverage expanded greatly, including more and thorough concurrent tests.
* Benchmarking.


### Final thoughts:
This all is strongly based on the idea/need for concurrent use of this service. Otherwise, the added complexity in the 
code as well as runtime really is not worth it.

