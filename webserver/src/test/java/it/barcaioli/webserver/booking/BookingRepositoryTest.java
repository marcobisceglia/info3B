package it.barcaioli.webserver.booking;

import it.barcaioli.webserver.boat.Boat;
import it.barcaioli.webserver.boat.BoatRepository;
import it.barcaioli.webserver.trip.Trip;
import it.barcaioli.webserver.trip.TripRepository;
import it.barcaioli.webserver.user.User;
import it.barcaioli.webserver.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository underTest;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private TripRepository tripRepository;

    @Test
    void findByTripIdOk() {
        // given
        User user = new User("Marco", "Bisceglia", "marco@gmail.com", "marco", "MMM");
        userRepository.save(user);

        Boat boat = new Boat("B1", 4);
        boatRepository.save(boat);

        LocalDateTime dayTrip = LocalDateTime.of(2022, Month.JANUARY, 01, 10, 00, 00);
        Trip trip = new Trip(dayTrip);
        tripRepository.save(trip);

        Booking expected = new Booking(5);
        expected.setUser(user);
        expected.setBoat(boat);
        expected.setTrip(trip);
        underTest.save(expected);

        // when
        List<Booking> result = underTest.findByTripId(1L);

        // then
        assertThat(expected).isEqualTo(result.get(0));
    }
}