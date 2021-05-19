package it.barcaioli.webserver.booking;

import java.util.Collections;
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
import it.barcaioli.webserver.boatsusage.BoatsUsage;
import it.barcaioli.webserver.boatsusage.BoatsUsageService;

@Service // More specific @Component
public class BookingService {

	private final BookingRepository bookingRepository;
	private final BoatsUsageService boatsUsageService;
	private final BoatService boatService;

	@Autowired
	public BookingService(BookingRepository bookingRepository, BoatService boatService,
			BoatsUsageService boatsUsageService) {
		this.bookingRepository = bookingRepository;
		this.boatsUsageService = boatsUsageService;
		this.boatService = boatService;
	}

	public List<Booking> getBookings() {
		return bookingRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	public Booking getBooking(Long id) {
		Optional<Booking> booking = bookingRepository.findById(id);

		if (!booking.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking doesn't exist");

		return booking.get();
	}

	private Booking getBookingByIds(Long userId, Long tripId) {

		List<Booking> bookings = bookingRepository.findAll().stream()
				.filter(booking -> booking.getUser().getId().equals(userId) && booking.getTrip().getId().equals(tripId))
				.collect(Collectors.toList());

		if (!bookings.isEmpty())
			return bookings.get(0);
		else
			return null;
	}

	public Booking createBooking(Booking booking) {

		// controllo che la prenotazione non esista già
		var b = getBookingByIds(booking.getUser().getId(), booking.getTrip().getId());

		if (b != null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking already exists");

		// algorithm that assing boat to the escursion
		assignBoat(booking);

		return bookingRepository.save(booking);
	}

	private void assignBoat(Booking booking) {
		System.out.println("Algoritmo avviato");

		var tripId = booking.getTrip().getId();
		var numPeople = booking.getNumPeople();
		var n = boatsUsageService.getTotalRemainingSeats(tripId);

		if (n > 0) {
			System.out.println(String.format("%d posti totali rimanenti per l'escursione", n));

			// prendo tutte le barche disponibili, già filtrate per contenere il gruppo, sia
			// quelle non utilizzate che quelle già
			// utilizzate
			List<Boat> usedBoatsWithEnoughSeats = boatsUsageService.getUsedBoatsWithEnoughSeats(tripId, numPeople);
			List<Boat> notUsedBoatsWithEnoughSeats = boatsUsageService.getNotUsedBoatsWithEnoughSeats(tripId, numPeople);

			if (!usedBoatsWithEnoughSeats.isEmpty()) {
				// esiste una barca NON VUOTA in grado di contenere il gruppo
				// ordinamento per numero di posto: prima le piccole poi le grandi
				Collections.sort(usedBoatsWithEnoughSeats);
				var chosenBoat = usedBoatsWithEnoughSeats.get(0); // la prima è la più piccola
				System.out.println("Scelta barca non vuota");

				// aggiorno campi tabella BoatUsage, in quanto barca era già presente
				// (tabella boat e trip si dovrebbero aggiornare da sole?)
				var boatUsage = boatsUsageService.getBoatsUsageByTripId(tripId).stream()
						.filter(bu -> bu.getBoat().equals(chosenBoat)).collect(Collectors.toList()).get(0);
				boatUsage.setAvailableSeats(boatUsage.getAvailableSeats() - numPeople);

			} else if (!notUsedBoatsWithEnoughSeats.isEmpty()) {
				// esiste una barca VUOTA in grado di contenere il gruppo
				// ordinamento per numero di posto: prima le piccole poi le grandi
				Collections.sort(notUsedBoatsWithEnoughSeats);
				var chosenBoat = notUsedBoatsWithEnoughSeats.get(0); // la prima è la più piccola
				System.out.println("Scelta barca vuota");

				// creo riga tabella BoatUsage, in quanto barca non era già presente
				// (tabella boat e trip si dovrebbero aggiornare da sole?)
				var boatUsage = new BoatsUsage();
				boatUsage.setBoat(chosenBoat);
				boatUsage.setTrip(booking.getTrip());
				boatUsage.setAvailableSeats(chosenBoat.getSeats() - numPeople);
				boatsUsageService.createBoatsUsage(boatUsage);

			} else {
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
						"Devo effettuare riallocazione, ma non ho ancora implementato");
			}
		} else {
			var numBoats = boatService.getBoats().stream().count();
			if (numBoats == 0) {
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Non ci sono barche nel sistema!");
			} else {
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
						String.format("OPS! Posti finiti nelle %d barche!", numBoats));
			}
		}
	}

	public Booking updateBooking(Long id, Booking booking) {
		Optional<Booking> bookingToUpdate = bookingRepository.findById(id);

		if (!bookingToUpdate.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No booking to update");

		var bookingFound = bookingToUpdate.get();

		bookingFound.setNumPeople(booking.getNumPeople());
		bookingFound.setUser(booking.getUser());
		bookingFound.setTrip(booking.getTrip());
		return bookingRepository.save(bookingFound);
	}

	public void deleteBooking(Long id) {
		Optional<Booking> bookingToDelete = bookingRepository.findById(id);

		if (!bookingToDelete.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No booking to delete");

		bookingRepository.delete(bookingToDelete.get());
	}
}
