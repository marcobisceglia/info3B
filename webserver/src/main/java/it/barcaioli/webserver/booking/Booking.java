package it.barcaioli.webserver.booking;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import it.barcaioli.webserver.boat.Boat;
import it.barcaioli.webserver.trip.Trip;
import it.barcaioli.webserver.user.User;

@Table(name = "Booking")
@Entity
public class Booking {

	private @Id @GeneratedValue Long id;
	private Integer numPeople;

	@ManyToOne // many bookings in a boat
	@JoinColumn(name = "boatId")
	private Boat boat;

	@ManyToOne // many bookings by a user
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne // many bookings for a trip
	@JoinColumn(name = "tripId")
	private Trip trip;

	Booking() {
	}

	public Booking(Integer numPeople, Boat boat, User user, Trip trip) {
		this.numPeople = numPeople;
		this.boat = boat;
		this.user = user;
		this.trip = trip;
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

	public Boat getBoat() {
		return boat;
	}

	public void setBoat(Boat boat) {
		this.boat = boat;
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
}
