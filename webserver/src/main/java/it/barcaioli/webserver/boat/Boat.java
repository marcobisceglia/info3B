package it.barcaioli.webserver.boat;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

import it.barcaioli.webserver.booking.Booking;

@Table(name = "Boat")
@Entity
public class Boat implements Comparable<Boat> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String model;
	private Integer seats;

	@JsonIgnore
	@ManyToMany(mappedBy = "boats", fetch = FetchType.LAZY)
	private List<Booking> bookings = new ArrayList<>();

	Boat() {
	}

	public Boat(String model, Integer seats) {
		super();
		this.model = model;
		this.seats = seats;
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

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	@Override
	public int compareTo(Boat o) {
		return this.seats - o.seats;
	}
}
