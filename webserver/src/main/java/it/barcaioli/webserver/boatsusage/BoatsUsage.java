package it.barcaioli.webserver.boatsusage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import it.barcaioli.webserver.boat.Boat;
import it.barcaioli.webserver.trip.Trip;

@Entity
@Table(name = "BoatsUsage")

public class BoatsUsage {

  private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;

  @ManyToOne
  @JoinColumn(name = "tripId")
  private Trip trip;

  @ManyToOne
  @JoinColumn(name = "boatId")
  private Boat boat;

  // additional field
  Integer availableSeats;

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

  public Integer getAvailableSeats() {
    return availableSeats;
  }

  public void setAvailableSeats(Integer availableSeats) {
    this.availableSeats = availableSeats;
  }
}
