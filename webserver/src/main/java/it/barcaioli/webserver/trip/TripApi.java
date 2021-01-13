package it.barcaioli.webserver.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TripApi {

    private final TripService tripService;
    
    @Autowired
    public TripApi(TripService tripService){
    	this.tripService = tripService;
    }

    @GetMapping("/trips")
    public Iterable<Trip> getTrips() {
        return tripService.getTrips();
    }
    
    @GetMapping("/trips/{id}")
    public Trip getTrip(@PathVariable Long id) {
        return tripService.getTrip(id);
    }

    @PostMapping("/trips")
    public Trip createTrip(@RequestBody Trip newTrip) {
        return tripService.createTrip(newTrip);
    }
    
    @PutMapping("/trips/{id}")
    public Trip updateTrip(@PathVariable("id") Long id, @RequestBody Trip trip) {
        return tripService.updateTrip(id, trip);
    }

    @DeleteMapping("/trips/{id}")
    public void deleteTrip(@PathVariable("id") Long id) {
        tripService.deleteTrip(id);
    }

}