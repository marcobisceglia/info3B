package it.barcaioli.webserver.boatsusage;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoatsUsageRepository extends JpaRepository<BoatsUsage, Long> {
  List<BoatsUsage> findByTripId(Long tripId);
}
