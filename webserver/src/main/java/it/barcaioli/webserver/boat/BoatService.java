package it.barcaioli.webserver.boat;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service // More specific @Component
public class BoatService {

	private final BoatRepository boatRepository;

	@Autowired
	public BoatService(BoatRepository boat) {
		this.boatRepository = boat;
	}

	public List<Boat> getBoats() {
		return boatRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	public Boat getBoat(Long id) {
		Optional<Boat> boat = boatRepository.findById(id);

		if (!boat.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Boat doesn't exist");

		return boat.get();
	}

	public Boat createBoat(Boat newBoat) {
		return boatRepository.save(newBoat);
	}

	public Boat updateBoat(Long id, Boat boat) {
		Optional<Boat> boatToUpdate = boatRepository.findById(id);

		if (!boatToUpdate.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No boat to update");

		var boatFound = boatToUpdate.get();

		boatFound.setModel(boat.getModel());
		boatFound.setSeats(boatToUpdate.get().getSeats()); // limit update of seats
		return boatRepository.save(boatFound);
	}

	public void deleteBoat(Long id) {
		Optional<Boat> boatToDelete = boatRepository.findById(id);

		if (!boatToDelete.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No boat to delete");

		boatRepository.delete(boatToDelete.get());
	}

	// Metodi aggiuntivi

	// Totale posti delle barche presenti
	public Integer getTotalSeats() {
		var boats = this.getBoats();
		return boats.stream().mapToInt(boat -> boat.getSeats()).sum();
	}

}
