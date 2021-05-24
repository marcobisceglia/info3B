package it.barcaioli.webserver.trip;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.barcaioli.webserver.boat.Boat;
import it.barcaioli.webserver.booking.Booking;
import it.barcaioli.webserver.booking.BookingDto;

@RestController
@RequestMapping(path = "trips")
public class TripController {

	private final TripService tripService;

	@Autowired
	public TripController(TripService tripService) {
		this.tripService = tripService;
	}

	private Trip dtoToEntity(TripDto tripDto) {
		var trip = new Trip();
		trip.setDateTime(tripDto.getDateTime());
		return trip;
	}

	@GetMapping
	public Iterable<Trip> getTrips(@RequestParam(required = false, name = "date") String dateTimeString) {
		if (dateTimeString == null) {
			return tripService.getTrips();
		} else {

			var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			var dateTime = LocalDate.parse(dateTimeString, formatter);

			return tripService.getTrips().stream().filter(trip -> trip.getDateTime().toLocalDate().equals(dateTime))
					.collect(Collectors.toList());
		}
	}

	@GetMapping(path = "{tripId}")
	public Trip getTrip(@PathVariable Long tripId) {
		return tripService.getTrip(tripId);
	}

	@GetMapping(path = "{tripId}/bookings")
	public List<BookingDto> getTripBookings(@PathVariable Long tripId) {
		List<BookingDto> bookingsDto = new ArrayList<>();
		tripService.getTrip(tripId).getBookings().forEach(b -> bookingsDto.add(b.entityToDto()));
		return bookingsDto;
	}

	@GetMapping(path = "{tripId}/boats")
	public List<Boat> getTripBoats(@PathVariable Long tripId) {
		List<Boat> boatsUsedByTrip = new ArrayList<>();
		tripService.getTrip(tripId).getBookings().forEach(b -> boatsUsedByTrip.add(b.getBoat()));
		return boatsUsedByTrip.stream().distinct().collect(Collectors.toList());
	}

	@PostMapping
	public Trip createTrip(@RequestBody TripDto tripDto) {
		var trip = dtoToEntity(tripDto);
		return tripService.createTrip(trip);
	}

	@PutMapping(path = "{tripId}")
	public Trip updateTrip(@PathVariable("tripId") Long tripId, @RequestBody TripDto tripDto) {
		var trip = dtoToEntity(tripDto);
		return tripService.updateTrip(tripId, trip);
	}

	@DeleteMapping(path = "{tripId}")
	public void deleteTrip(@PathVariable("tripId") Long tripId) {
		tripService.deleteTrip(tripId);
	}
}
