package it.barcaioli.webserver.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerData extends CrudRepository<Customer, Long>{

}
