package it.barcaioli.webserver.trip;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
  List<Trip> findByDateTime(LocalDateTime dateTime);
}
