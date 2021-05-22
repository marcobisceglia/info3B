package it.barcaioli.webserver.booking;

public class BookingDto {
  private Long id;
  private Long userId;
  private Long tripId;
  private Integer numPeople;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public Integer getNumPeople() {
    return numPeople;
  }

  public void setNumPeople(Integer numPeople) {
    this.numPeople = numPeople;
  }
}
