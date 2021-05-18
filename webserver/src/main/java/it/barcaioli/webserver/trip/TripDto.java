package it.barcaioli.webserver.trip;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class TripDto {
  @JsonFormat(pattern = "dd-MM-yyyy HH:mm", timezone = "Europe/Paris")
  private LocalDateTime dateTime;

  public LocalDateTime getDateTime() {
    return dateTime;
  }

  public void setDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime;
  }
}
