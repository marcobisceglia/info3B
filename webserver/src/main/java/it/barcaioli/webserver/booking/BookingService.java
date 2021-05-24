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

	private static final String TOOSMALLGROUP = "FAIL. Posti non terminati, ma sparsi e il gruppo è troppo piccolo per essere diviso";
	private static final String GROUPDIVISIONFAIL = "FAIL. Posti non terminati, ma sparsi. Anche dividendo il gruppo non è stato possibile allocare barche";
	private static final String NOBOATS = "FAIL. Barche piene o assenti!";
	private static final String EMPTYBOATFOUND = "SUCCESS. Scelta barca vuota";
	private static final String USEDBOATFOUND = "SUCCESS. Scelta barca già in uso";

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
		// NB: nel db lo stesso utente può comunque avere più righe per la stessa trip,
		// in quanto può avere due gruppi separati su due barche diverse
		var b = getBookingByIds(booking.getUser().getId(), booking.getTrip().getId());

		if (b != null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking already exists");

		// divided è true se la prenotazione necessita di 2 barche
		var divided = assignBoats(booking);

		List<Boat> boats = new ArrayList<>();

		if (divided.booleanValue())
			boats.add(this.getBookings().get(this.getBookings().size() - 2).getBoat());

		boats.add(this.getBookings().get(this.getBookings().size() - 1).getBoat());

		return boats;
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

	// Mappa per tenere traccia dei posti rimanenti per ogni barca utilizzata
	// nell'escursione
	public Map<Boat, Integer> getUsedBoatsByTripId(Long id) {

		HashMap<Boat, Integer> mappa = new HashMap<>();

		List<Booking> bookings = bookingRepository.findByTripId(id);

		for (var booking : bookings) {
			var boat = booking.getBoat();
			// se la mappa non contiene la barca la inserisco
			// altrimenti aggiorno la disponibilità
			if (!mappa.containsKey(boat)) {
				mappa.put(boat, boat.getSeats() - booking.getNumPeople());
			} else {
				mappa.put(boat, mappa.get(boat) - booking.getNumPeople());
			}

		}
		return mappa;
	}

	// Ritorna la stessa mappa, ma solo con le barche con abbastanza posti rimanenti
	// per contenere num
	private Map<Boat, Integer> filterMap(Map<Boat, Integer> map, Integer num) {
		return map.entrySet().stream().filter(m -> m.getValue() >= num)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	// Ritorna la lista di barche presenti nella mappa
	private List<Boat> getBoatsFromMap(Map<Boat, Integer> map) {
		List<Boat> list = new ArrayList<>();
		map.entrySet().stream().forEach(e -> list.add(e.getKey()));
		return list;
	}

	// Data una prenotazione, prende l'escursione a cui si riferisce e ritorna tutte
	// le barche già utilizzate, ordinate per numero di posto e in grado di
	// contenere il numero di persone indicato
	private List<Boat> getUsedBoatsWithEnoughSeats(Booking booking, Integer numPeople) {
		List<Boat> usedBoatsWithEnoughSeats = new ArrayList<>();

		// barche già utilizzate nell'escursione
		Map<Boat, Integer> usedBoatsMap = getUsedBoatsByTripId(booking.getTrip().getId());

		// filtro le barche in modo di avere solo quelle in grado di contenere il gruppo
		Map<Boat, Integer> usedBoatsWithEnoughSeatsMap = filterMap(usedBoatsMap, numPeople);

		// se è vuota ritorna la lista vuota
		if (!usedBoatsWithEnoughSeatsMap.isEmpty()) {
			usedBoatsWithEnoughSeats = getBoatsFromMap(usedBoatsWithEnoughSeatsMap);

			// ordinamento per numero di posto: prima le piccole poi le grandi
			Collections.sort(usedBoatsWithEnoughSeats);
		}

		return usedBoatsWithEnoughSeats;
	}

	// Ritorna tutte le barche non ancora utilizzate per l'escursione a cui si
	// riferisce la prenotazione, con abbastanza posti rimanenti e ordinate per
	// numero di posto
	private List<Boat> getEmptyBoatsWithEnoughSeats(Booking booking, Integer numPeople) {

		// barche vuote = tutte le barche - barche utilizzate
		List<Boat> allBoats = boatService.getBoats();
		var usedBoatsMap = getUsedBoatsByTripId(booking.getTrip().getId());
		List<Boat> emptyBoats = new ArrayList<>();

		usedBoatsMap.entrySet().stream().forEach(e -> allBoats.remove(e.getKey()));

		if (!allBoats.isEmpty()) {

			// filtro per numero di posto
			emptyBoats = allBoats.stream().filter(boat -> boat.getSeats().compareTo(numPeople) >= 0)
					.collect(Collectors.toList());

			// ordinamento per numero di posto: prima le piccole poi le grandi
			Collections.sort(emptyBoats);
		}
		return emptyBoats;
	}

	// Assegna una barca al gruppo per l'escursione, se possibile.
	// Se una barca non basta, cerca di dividere il gruppo e assegnare due barche,
	// se possibile.
	// Ritorna true se il gruppo è stato diviso
	private Boolean assignBoats(Booking booking) {
		System.out.println("\nAlgoritmo prenotazione avviato. Utente " + booking.getUser().getId() + ". Escursione "
				+ booking.getTrip().getId() + ". N persone: " + booking.getNumPeople());

		var divided = false;
		var tripId = booking.getTrip().getId();
		var numPeople = booking.getNumPeople();
		var n = getRemainingSeatsByTripId(tripId);

		// se i posti sono finiti o non ci sono barche nel sistema segnalo errore
		if (n > 0)
			System.out.println(String.format("%d Posti totali rimanenti per l'escursione", n));
		else {
			System.out.println(NOBOATS);
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, NOBOATS);
		}

		// cerco una barca NON VUOTA con abbastanza posti rimanenti per il gruppo
		List<Boat> usedBoatsWithEnoughSeats = getUsedBoatsWithEnoughSeats(booking, numPeople);

		// se esiste salvo la barca e termino la funzione
		if (!usedBoatsWithEnoughSeats.isEmpty()) {
			var chosenBoat = usedBoatsWithEnoughSeats.get(0); // la prima è la più piccola
			booking.setBoat(chosenBoat);
			bookingRepository.save(booking);
			System.out.println(USEDBOATFOUND + ": " + chosenBoat.getModel() + " da " + chosenBoat.getSeats() + " posti");
			return divided;
		}

		// cerco una barca VUOTA con abbastanza posti rimanenti per il gruppo
		var emptyBoats = getEmptyBoatsWithEnoughSeats(booking, numPeople);

		// se esiste salvo la barca e termino la funzione
		if (!emptyBoats.isEmpty()) {
			var chosenBoat = emptyBoats.get(0); // la prima è la più piccola
			booking.setBoat(chosenBoat);
			bookingRepository.save(booking);
			System.out.println(EMPTYBOATFOUND + ": " + chosenBoat.getModel() + " da " + chosenBoat.getSeats() + " posti");
			return divided;
		}

		System.out.println("Provo a dividere il gruppo e trovare 2 barche");

		// il gruppo non può essere contenuto allo stato attuale delle barche
		// quindi si prova a dividere il gruppo in due, ma solo se il gruppo è composto
		// da almeno 4 persone
		if (numPeople < 4) {
			System.out.println(TOOSMALLGROUP);
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, TOOSMALLGROUP);
		}

		Boat firstBoat;
		Boat secondBoat;

		// dimensioni dei due gruppi
		Integer group2 = numPeople / 2;
		Integer group1 = 0;
		if (numPeople % 2 == 0)
			group1 = group2;
		else
			group1 = numPeople / 2 + 1;

		System.out.println("Gruppo 1: " + group1 + " persone\nGruppo 2: " + group2 + " persone");

		// ci devono essere almeno due barche (vuote o non vuote) in grado di contenere
		// i due gruppi. Si prendono tutte le barche in grado di contenere il gruppo 2,
		// che in caso di gruppo dispari, è il gruppo più piccolo
		List<Boat> allBoatsForGroups = new ArrayList<>();
		allBoatsForGroups.addAll(getUsedBoatsWithEnoughSeats(booking, group2));
		allBoatsForGroups.addAll(getEmptyBoatsWithEnoughSeats(booking, group2));

		// se non ci sono due barche in grado di contenere il gruppo 2, allora
		// sicuramente non c'è modo di mettere nemmeno il primo, quindi la funzione
		// termina
		if (allBoatsForGroups.size() < 2) {
			System.out.println(GROUPDIVISIONFAIL);
			throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, GROUPDIVISIONFAIL);
		}

		// se i gruppi sono uguali basta prendere le prime due barche più piccole
		// altrimenti devo fare un ulteriore controllo, in quanto per il gruppo 1 ho
		// bisogno di un posto aggiuntivo
		if (group1.equals(group2)) {
			firstBoat = allBoatsForGroups.get(0);
			secondBoat = allBoatsForGroups.get(1);
		} else {
			secondBoat = allBoatsForGroups.get(0);
			allBoatsForGroups.remove(secondBoat);

			final Integer temp = group1;
			allBoatsForGroups.stream().filter(boat -> boat.getSeats() >= temp).collect(Collectors.toList());

			// se non esiste barca per contenere il gruppo 1 la funzione termina
			if (allBoatsForGroups.isEmpty()) {
				System.out.println(GROUPDIVISIONFAIL);
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, GROUPDIVISIONFAIL);
			}

			firstBoat = allBoatsForGroups.get(0);
		}

		System.out.println(
				"SUCCESS. Scelta barca " + firstBoat.getModel() + " da " + firstBoat.getSeats() + " posti per gruppo 1");
		System.out.println(
				"SUCCESS. Scelta barca " + secondBoat.getModel() + " da " + secondBoat.getSeats() + " posti per gruppo 2");

		// creo due prenotazioni per lo stesso utente, con barche diverse e gruppi
		// separati
		booking.setBoat(firstBoat);
		booking.setNumPeople(numPeople - group2);
		bookingRepository.save(booking);

		var secondBooking = new Booking();
		secondBooking.setUser(booking.getUser());
		secondBooking.setTrip(booking.getTrip());
		secondBooking.setBoat(secondBoat);
		secondBooking.setNumPeople(numPeople - group1);
		bookingRepository.save(secondBooking);

		divided = true;

		return divided;
	}
}
