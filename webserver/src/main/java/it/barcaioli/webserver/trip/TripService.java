package it.barcaioli.webserver.trip;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service //A @Component more specific
public class TripService {

	private final TripData tripData;

	public TripService(TripData trip){
    	this.tripData = trip;
    }

	public Iterable<Trip> getTrips() {
    	//findAll() it's auto generated by SpringBoot
        return tripData.findAll();
    }
    
	public Trip getTrip(Long id) {
    	//findById() it's auto generated by SpringBoot
		Optional<Trip> trip = tripData.findById(id);
		
		if (!trip.isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trip doesn't exist");
		
        return trip.get();
    }

	public Trip createTrip(Trip newTrip) {
		//save() it's auto generated by SpringBoot
        return tripData.save(newTrip);
    }
    
	public Trip updateTrip(Long id, Trip trip) {
		Optional<Trip> tripToUpdate = tripData.findById(id);
        
        if(!tripToUpdate.isPresent())
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No trip to update");

        Trip tripFound = tripToUpdate.get();
        
        tripFound.setDateTime(trip.getDateTime());
        return tripData.save(tripFound);
    }

	public void deleteTrip(Long id) {
		Optional<Trip> tripToDelete = tripData.findById(id);
		
		if (!tripToDelete.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No trip to delete");
		
        //delete() it's auto generated by SpringBoot
        tripData.delete(tripToDelete.get());
    }
}
