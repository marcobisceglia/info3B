package it.barcaioli.webserver.trip;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service // More specific @Component
public class TripService {

	private final TripRepository tripRepository;

	@Autowired
	public TripService(TripRepository trip) {
		this.tripRepository = trip;
	}

	public List<Trip> getTrips() {
		return tripRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	public Trip getTrip(Long id) {
		Optional<Trip> trip = tripRepository.findById(id);

		if (!trip.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip doesn't exist");

		return trip.get();
	}

	public Trip createTrip(Trip newTrip) {

		List<Trip> trips = tripRepository.findByDateTime(newTrip.getDateTime());

		if (!trips.isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trip already exists");

		if (newTrip.getDateTime().isBefore(LocalDateTime.now()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trip can't be done in the past");

		return tripRepository.save(newTrip);
	}

	public Trip updateTrip(Long id, Trip trip) {
		Optional<Trip> tripToUpdate = tripRepository.findById(id);

		if (!tripToUpdate.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No trip to update");

		var tripFound = tripToUpdate.get();

		tripFound.setDateTime(trip.getDateTime());
		return tripRepository.save(tripFound);
	}

	public void deleteTrip(Long id) {
		Optional<Trip> tripToDelete = tripRepository.findById(id);

		if (!tripToDelete.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No trip to delete");

		tripRepository.delete(tripToDelete.get());
	}
}
