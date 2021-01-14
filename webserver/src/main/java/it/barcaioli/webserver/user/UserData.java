package it.barcaioli.webserver.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserData extends CrudRepository<User, Long>{

}
