package it.barcaioli.webserver.boatsusage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoatsUsageRepository extends JpaRepository<BoatsUsage, Long> {
}
