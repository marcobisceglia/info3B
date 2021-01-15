package it.barcaioli.webserver.boat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoatData extends JpaRepository<Boat, Long>{

}
