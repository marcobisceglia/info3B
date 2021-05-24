package it.barcaioli.webserver.booking;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.barcaioli.webserver.trip.TripService;
import it.barcaioli.webserver.user.UserService;

@RestController
@RequestMapping(path = "bookings")
public class BookingController {

	private final BookingService bookingService;
	private final UserService userService;
	private final TripService tripService;

	@Autowired
	public BookingController(BookingService bookingService, UserService userService, TripService tripService) {
		this.bookingService = bookingService;
		this.userService = userService;
		this.tripService = tripService;
	}

	private Booking dtoToEntity(BookingDto bookingDto) {
		var booking = new Booking();

		booking.setNumPeople(bookingDto.getNumPeople());
		booking.setUser(userService.getUser(bookingDto.getUserId()));
		booking.setTrip(tripService.getTrip(bookingDto.getTripId()));

		return booking;
	}

	@GetMapping
	public Iterable<BookingDto> getBookings() {
		List<BookingDto> bookingsDto = new ArrayList<>();
		bookingService.getBookings().forEach(b -> bookingsDto.add(b.entityToDto()));
		return bookingsDto;
	}

	@GetMapping(path = "{bookingId}")
	public BookingDto getBooking(@PathVariable Long bookingId) {
		return bookingService.getBooking(bookingId).entityToDto();
	}

	@PostMapping
	public List<String> createBooking(@RequestBody BookingDto bookingDto) {
		var booking = dtoToEntity(bookingDto);
		return bookingService.createBooking(booking);
	}

	@DeleteMapping(path = "{bookingId}")
	public void deleteBooking(@PathVariable("bookingId") Long bookingId) {
		bookingService.deleteBooking(bookingId);
	}
}
