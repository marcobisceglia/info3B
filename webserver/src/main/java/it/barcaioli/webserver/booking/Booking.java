package it.barcaioli.webserver.booking;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Table(name = "Booking")
@Entity
public class Booking {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
	private Integer numPeople;
	private Long userId;
	private Long tripId;
	@JsonProperty(access = Access.READ_ONLY)
	private Long boatId;

	Booking() {
	}

	public Booking(Integer numPeople, Long userId, Long tripId, Long boatId) {
		this.numPeople = numPeople;
		this.userId = userId;
		this.tripId = tripId;
		this.boatId = boatId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public Long getBoatId() {
		return boatId;
	}

	public void setBoatId(Long boatId) {
		this.boatId = boatId;
	}
}
