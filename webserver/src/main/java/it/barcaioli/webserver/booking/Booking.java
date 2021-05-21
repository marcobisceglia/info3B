package it.barcaioli.webserver.booking;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

	@ManyToOne // many bookings by a user
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne // many bookings for a trip
	@JoinColumn(name = "tripId")
	private Trip trip;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "boats_bookings", joinColumns = {
			@JoinColumn(name = "bookingId", referencedColumnName = "id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "boatId", referencedColumnName = "id", nullable = false, updatable = false) })
	private List<Boat> boats = new ArrayList<>();

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

	public List<Boat> getBoats() {
		return boats;
	}

	public void setBoats(List<Boat> boats) {
		this.boats = boats;
	}

	public void addBoat(Boat boat) {
		this.boats.add(boat);
	}

	public void removeBoat(Boat boat) {
		this.boats.remove(boat);
	}

	@Override
	public String toString() {
		return "Booking [boats=" + boats + ", id=" + id + ", numPeople=" + numPeople + ", trip=" + trip + ", user=" + user
				+ "]";
	}

}
