package it.barcaioli.webserver.booking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
  List<Booking> findByTripId(Long tripId);

  List<Booking> findByUserIdAndTripId(Long userId, Long tripId);

}
