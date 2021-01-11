package it.barcaioli.webserver.boat;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoatData extends CrudRepository<Boat, Long>{

}
