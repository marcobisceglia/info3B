package it.barcaioli.webserver.boat;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.barcaioli.webserver.booking.Booking;

@Table(name = "Boat")
@Entity
public class Boat {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String model;
	private Integer numSeats;

	// 1 Boat can have MANY Bookings
	// mappedBy Booking class private field
	// Each operation on the boat will be propagated to the bookings
	// Bookings are removed if boat is removed
	@OneToMany(mappedBy = "boat", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Booking> bookings;

	Boat() {
	}

	public Boat(String model, Integer numSeats) {
		super();
		this.model = model;
		this.numSeats = numSeats;
		this.bookings = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getNumSeats() {
		return numSeats;
	}

	public void setNumSeats(Integer numSeats) {
		this.numSeats = numSeats;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public void addBooking(Booking booking) {
		this.bookings.add(booking);
	}

	public void removeBooking(Booking booking) {
		this.bookings.remove(booking);
	}
}
