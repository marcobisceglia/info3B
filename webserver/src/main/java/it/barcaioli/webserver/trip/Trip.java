package it.barcaioli.webserver.trip;

import java.time.*;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.barcaioli.webserver.boatsusage.BoatsUsage;
import it.barcaioli.webserver.booking.Booking;

@Table(name = "Trip")
@Entity
public class Trip {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Europe/Paris")
	private LocalDateTime dateTime;

	// 1 Trip can have many Bookings
	// mappedBy Trip class private field
	// Each operation on the trip will be propagated to the bookings
	// Bookings are removed if trip is removed
	@JsonIgnore
	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Booking> bookings;

	// 1 Trip can use many Boats
	// mappedBy BoatsUsage class private field
	// Each operation on the trip will be propagated to the boatsUsage
	// BoatsUsage are removed if trip is removed
	@JsonIgnore
	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BoatsUsage> boatsUsage;

	Trip() {
	}

	public Trip(LocalDateTime dateTime) {
		super();
		this.dateTime = dateTime;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public List<BoatsUsage> getBoatsUsage() {
		return boatsUsage;
	}

	public void setBoatsUsage(List<BoatsUsage> boatsUsage) {
		this.boatsUsage = boatsUsage;
	}
}
