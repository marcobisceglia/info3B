package it.barcaioli.webserver.trip;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import it.barcaioli.webserver.booking.Booking;

@Table(name = "Trip")
@Entity
public class Trip {

	private @Id @GeneratedValue Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Europe/Paris")
	private Date dateTime;

	// 1 Trip can have MANY Bookings
	// mappedBy Booking class private field
	// Each operation on the trip will be propagated to the bookings
	// Bookings are removed if trip is removed
	@OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Booking> bookings;

	Trip() {
	}

	public Trip(Date dateTime) {
		super();
		this.dateTime = dateTime;
		this.bookings = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
}
