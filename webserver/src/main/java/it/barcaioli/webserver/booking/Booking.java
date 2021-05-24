package it.barcaioli.webserver.booking;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

import it.barcaioli.webserver.boat.Boat;
import it.barcaioli.webserver.trip.Trip;
import it.barcaioli.webserver.user.User;

@Table(name = "Booking")
@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer numPeople;

	@JsonIgnore
	@ManyToOne // many bookings by a user
	@JoinColumn(name = "userId")
	private User user;

	@JsonIgnore
	@ManyToOne // many bookings for a trip
	@JoinColumn(name = "tripId")
	private Trip trip;

	@JsonIgnore
	@ManyToOne // many bookings for a boat
	@JoinColumn(name = "boatId")
	private Boat boat;

	Booking() {
	}

	public Booking(Integer numPeople) {
		this.numPeople = numPeople;
	}

	public Long getId() {
		return id;
	}

	public Integer getNumPeople() {
		return numPeople;
	}

	public void setNumPeople(Integer numPeople) {
		this.numPeople = numPeople;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Trip getTrip() {
		return trip;
	}

	public void setTrip(Trip trip) {
		this.trip = trip;
	}

	public Boat getBoat() {
		return boat;
	}

	public void setBoat(Boat boat) {
		this.boat = boat;
	}

	@Override
	public String toString() {
		return "Booking [boat=" + boat + ", id=" + id + ", numPeople=" + numPeople + ", trip=" + trip + ", user=" + user
				+ "]";
	}

	public BookingDto entityToDto() {
		var bookingDto = new BookingDto();

		bookingDto.setId(this.getId());
		bookingDto.setNumPeople(this.getNumPeople());
		bookingDto.setTripId(this.getTrip().getId());
		bookingDto.setUserId(this.getUser().getId());

		return bookingDto;
	}
}
