package it.barcaioli.webserver.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "trips")
public class TripController {

	private final TripService tripService;

	@Autowired
	public TripController(TripService tripService) {
		this.tripService = tripService;
	}

	@GetMapping
	public Iterable<Trip> getTrips() {
		return tripService.getTrips();
	}

	@GetMapping(path = "{tripId}")
	public Trip getTrip(@PathVariable Long tripId) {
		return tripService.getTrip(tripId);
	}

	@PostMapping
	public Trip createTrip(@RequestBody Trip trip) {
		return tripService.createTrip(trip);
	}

	@PutMapping(path = "{tripId}")
	public Trip updateTrip(@PathVariable("tripId") Long tripId, @RequestBody Trip trip) {
		return tripService.updateTrip(tripId, trip);
	}

	@DeleteMapping(path = "{tripId}")
	public void deleteTrip(@PathVariable("tripId") Long tripId) {
		tripService.deleteTrip(tripId);
	}
}
