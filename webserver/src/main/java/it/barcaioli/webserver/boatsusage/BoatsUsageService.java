package it.barcaioli.webserver.boatsusage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.barcaioli.webserver.boat.Boat;
import it.barcaioli.webserver.boat.BoatService;

@Service // More specific @Component
public class BoatsUsageService {

	private final BoatsUsageRepository boatsUsageRepository;
	private final BoatService boatService;

	@Autowired
	public BoatsUsageService(BoatsUsageRepository boatsUsage, BoatService boatService) {
		this.boatsUsageRepository = boatsUsage;
		this.boatService = boatService;
	}

	public List<BoatsUsage> getBoatsUsage() {
		return boatsUsageRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	public BoatsUsage getBoatsUsage(Long id) {
		Optional<BoatsUsage> boatsUsage = boatsUsageRepository.findById(id);

		if (!boatsUsage.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "BoatsUsage doesn't exist");

		return boatsUsage.get();
	}

	public BoatsUsage createBoatsUsage(BoatsUsage newBoatsUsage) {
		return boatsUsageRepository.save(newBoatsUsage);
	}

	public BoatsUsage updateBoatsUsage(Long id, BoatsUsage boatsUsage) {
		Optional<BoatsUsage> boatsUsageToUpdate = boatsUsageRepository.findById(id);

		if (!boatsUsageToUpdate.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No boatsUsage to update");

		var boatsUsageFound = boatsUsageToUpdate.get();

		boatsUsageFound.setBoat(boatsUsage.getBoat());
		boatsUsageFound.setTrip(boatsUsage.getTrip());
		boatsUsageFound.setAvailableSeats(boatsUsage.getAvailableSeats());

		return boatsUsageRepository.save(boatsUsageFound);
	}

	public void deleteBoatsUsage(Long id) {
		Optional<BoatsUsage> boatsUsageToDelete = boatsUsageRepository.findById(id);

		if (!boatsUsageToDelete.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Boat Usage to delete");

		boatsUsageRepository.delete(boatsUsageToDelete.get());
	}

	// Da qui partono i metodi aggiuntivi

	// Ritorna la lista dell'utilizzo barche utilizzate nell'escursione specificata
	public List<BoatsUsage> getBoatsUsageByTripId(Long tripId) {
		return boatsUsageRepository.findByTripId(tripId);
	}

	// Ritorna la lista di barche già utilizzate nell'escursione
	public List<Boat> getUsedBoatsByTripId(Long tripId) {
		List<BoatsUsage> boatsUsage = getBoatsUsageByTripId(tripId);
		List<Boat> usedBoats = new ArrayList<>();
		for (BoatsUsage boatUsage : boatsUsage) {
			usedBoats.add(boatUsage.getBoat());
		}
		return usedBoats;
	}

	// Ritorna la lista di barche non ancora utilizzate nell'escursione
	public List<Boat> getNotUsedBoatsByTripId(Long tripId) {
		// insieme di tutte le barche presenti nel sistema
		List<Boat> allBoats = boatService.getBoats();

		// insieme di barche già utilizzate
		List<Boat> usedBoats = getUsedBoatsByTripId(tripId);

		// tolgo dall'insieme di tutte le barche quelle già utilizzate
		for (Boat usedBoat : usedBoats) {
			allBoats.remove(usedBoat);
		}

		return allBoats;
	}

	// Ritorna la lista di barche già utilizzate nell'escursione, ma solo quelle in
	// grado di contenere il numero di persone indicato
	public List<Boat> getUsedBoatsWithEnoughSeats(Long tripId, Integer numPeople) {
		List<BoatsUsage> boatsUsage = getBoatsUsageByTripId(tripId);
		List<Boat> usedBoats = new ArrayList<>();
		for (BoatsUsage boatUsage : boatsUsage) {
			if (boatUsage.getAvailableSeats() >= numPeople) {
				usedBoats.add(boatUsage.getBoat());
			}
		}
		return usedBoats;
	}

	// Ritorna la lista di barche non utilizzate nell'escursione, ma solo quelle in
	// grado di contenere il numero di persone indicato
	public List<Boat> getNotUsedBoatsWithEnoughSeats(Long tripId, Integer numPeople) {
		return getNotUsedBoatsByTripId(tripId).stream().filter(boat -> boat.getSeats() >= numPeople)
				.collect(Collectors.toList());
	}

	// Ritorna i posti rimanenenti totali per l'escursione
	public Integer getTotalRemainingSeats(Long tripId) {
		// posti disponibili su tutte le barche
		Integer totalSeats = boatService.getTotalSeats();

		// barche già utilizzate in questa escursione
		List<BoatsUsage> boatsUsage = getBoatsUsageByTripId(tripId);

		for (BoatsUsage b : boatsUsage) {
			// tolgo il posto già considerato sopra e aggiungo quelli disponibili
			totalSeats = totalSeats - b.getBoat().getSeats() + b.getAvailableSeats();
		}
		return totalSeats;
	}
}
