package it.barcaioli.webserver.trip;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripData extends CrudRepository<Trip, Long>{

}
