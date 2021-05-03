package it.barcaioli.webserver.booking;

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
@RequestMapping(path = "bookings")
public class BookingController {

  private final BookingService bookingService;

  @Autowired
  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @GetMapping
  public Iterable<Booking> getBookings() {
    return bookingService.getBookings();
  }

  @GetMapping(path = "{bookingId}")
  public Booking getBooking(@PathVariable Long bookingId) {
    return bookingService.getBooking(bookingId);
  }

  @PostMapping()
  public Booking createBooking(@RequestBody Booking booking) {
    return bookingService.createBooking(booking);
  }

  @PutMapping(path = "{bookingId}")
  public Booking updateBooking(@PathVariable("bookingId") Long bookingId, @RequestBody Booking booking) {
    return bookingService.updateBooking(bookingId, booking);
  }

  @DeleteMapping(path = "{bookingId}")
  public void deleteBooking(@PathVariable("bookingId") Long bookingId) {
    bookingService.deleteBooking(bookingId);
  }
}
