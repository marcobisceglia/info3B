package it.barcaioli.webserver.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripData extends JpaRepository<Trip, Long>{

}
