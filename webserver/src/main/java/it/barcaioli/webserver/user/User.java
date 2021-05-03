package it.barcaioli.webserver.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import it.barcaioli.webserver.booking.Booking;

@Table(name = "`User`")
@Entity
public class User {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private String firstName;
	private String lastName;
	@NotBlank
	private String email;
	@NotBlank
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	private String license;
	private boolean loggedIn;

	// 1 User can do MANY Bookings
	// mappedBy Booking class private field
	// Each operation on the user will be propagated to the bookings
	// Bookings are removed if user is removed
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Booking> bookings;

	User() {
	}

	public User(String firstName, String lastName, @NotBlank String email, @NotBlank String password, String license) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.license = license;
		this.loggedIn = false;
		this.bookings = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
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
