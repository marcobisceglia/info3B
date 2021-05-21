package it.barcaioli.webserver.booking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class BookingService {

	private final BookingRepository bookingRepository;

	private final BoatService boatService;

	@Autowired
	public BookingService(BookingRepository bookingRepository, BoatService boatService) {
		this.bookingRepository = bookingRepository;
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

		List<Booking> bookings = bookingRepository.findByUserIdAndTripId(userId, tripId);

		if (!bookings.isEmpty())
			return bookings.get(0);
		else
			return null;
	}

	public List<Boat> createBooking(Booking booking) {

		// controllo che la prenotazione per l'escursione non esista già per l'utente
		var b = getBookingByIds(booking.getUser().getId(), booking.getTrip().getId());

		if (b != null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking already exists");

		// algorithm that assing boat to the escursion
		assignBoat(booking);

		return bookingRepository.save(booking).getBoats();
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

	// Metodi aggiuntivi

	// Posti barca occupati in un'escursione
	public Integer getOccupiedSeatsByTripId(Long id) {
		List<Booking> bookings = bookingRepository.findByTripId(id);
		return bookings.stream().mapToInt(booking -> booking.getNumPeople()).sum();
	}

	// Posti barca rimanenti per un'escursione
	public Integer getRemainingSeatsByTripId(Long id) {
		// Somma di tutti i posti barca - posti occupati nell'escursione
		return boatService.getTotalSeats() - getOccupiedSeatsByTripId(id);
	}

	// Mappa contenente tutte le barche utilizzate in un'escursione con la relativa
	// disponibilità
	public Map<Boat, Integer> getUsedBoatsByTripId(Long id) {

		// mappa per tenere traccia dei posti rimanenti per ogni barca utilizzata
		HashMap<Boat, Integer> mappa = new HashMap<>();

		List<Booking> bookings = bookingRepository.findByTripId(id);

		for (var booking : bookings) {
			var boatss = booking.getBoats();
			for (var boat : boatss) {
				// se la mappa non contiene la barca la inserisco
				if (!mappa.containsKey(boat)) {
					mappa.put(boat, boat.getSeats() - booking.getNumPeople());
				} else { // aggiorno disponibilità
					mappa.put(boat, mappa.get(boat) - booking.getNumPeople());
				}
			}
		}
		return mappa;
	}

	private void assignBoat(Booking booking) {
		System.out.println("Algoritmo avviato");

		var tripId = booking.getTrip().getId();
		var numPeople = booking.getNumPeople();
		var n = getRemainingSeatsByTripId(tripId);

		// se ci sono posti rimanenti continuo
		// altrimenti o sono finiti i posti o non ci sono barche nel sistema
		if (n > 0) {
			System.out.println(String.format("%d posti totali rimanenti per l'escursione", n));
		} else {
			var numBoats = boatService.getBoats().stream().count();
			if (numBoats == 0) {
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Non ci sono barche nel sistema!");
			} else {
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
						String.format("OPS! Le %d barche sono piene!", numBoats));
			}
		}

		// barche già utilizzate nell'escursione
		Map<Boat, Integer> usedBoatsMap = getUsedBoatsByTripId(booking.getTrip().getId());

		// filtro le barche in modo di avere solo quelle in grado di contenere il gruppo
		Map<Boat, Integer> usedBoatsWithEnoughSeatsMap = usedBoatsMap.entrySet().stream()
				.filter(map -> map.getValue() >= numPeople).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

		// se esiste almeno una barca NON VUOTA in grado di contenere il gruppo
		// altrimenti cerco tra le barche vuote
		if (!usedBoatsWithEnoughSeatsMap.isEmpty()) {

			// prendo le barche dalla mappa
			List<Boat> usedBoatsWithEnoughSeats = new ArrayList<>();
			usedBoatsWithEnoughSeatsMap.entrySet().stream().forEach(e -> usedBoatsWithEnoughSeats.add(e.getKey()));

			// ordinamento per numero di posto: prima le piccole poi le grandi
			Collections.sort(usedBoatsWithEnoughSeats);

			var chosenBoat = usedBoatsWithEnoughSeats.get(0); // la prima è la più piccola
			booking.addBoat(chosenBoat);
			System.out.println("Scelta barca non vuota");

		} else {
			// barche vuote per l'escursione = tutte le barche - barche utilizzate
			var emptyBoats = boatService.getBoats();
			usedBoatsMap.entrySet().stream().forEach(e -> emptyBoats.remove(e.getKey()));

			// se esiste una barca VUOTA in grado di contenere il gruppo
			// altrimenti devo riallocare
			if (!emptyBoats.isEmpty()) {

				// ordinamento per numero di posto: prima le piccole poi le grandi
				Collections.sort(emptyBoats);

				var chosenBoat = emptyBoats.get(0); // la prima è la più piccola
				booking.addBoat(chosenBoat);
				System.out.println("Scelta barca vuota");

			} else {
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
						"Devo effettuare riallocazione, ma non ho ancora implementato");
			}
		}
	}
}
