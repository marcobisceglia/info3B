package it.barcaioli.webserver.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.barcaioli.webserver.boat.Boat;
import it.barcaioli.webserver.boat.BoatRepository;
import it.barcaioli.webserver.boat.BoatService;
import it.barcaioli.webserver.trip.Trip;
import it.barcaioli.webserver.trip.TripService;
import it.barcaioli.webserver.user.User;
import it.barcaioli.webserver.user.UserRepository;
import it.barcaioli.webserver.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private BoatService boatService;

    @InjectMocks
    BookingService underTest;

    @Test
    void createBooking() throws Exception {
        User u1 = new User("Giulia","Sonzogni","giulia@gmail.com","giulia","GGG");
        User u2 = new User("Marco","Bisceglia","marco@gmail.com","marco","MMM");
        User u3 = new User("Linda","Frickleton","linda@gmail.com","linda","LLL");
        User u4 = new User("Pippo","Franco","pippo@gmail.com","pippo","PPP");
        User u5 = new User("Valentino","Rossi","valentino@gmail.com","valentino","VVV");
        User u6 = new User("Alessandro","Del Piero","alessandro@gmail.com","alessandro","AAA");

        List<User> users = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
        users.add(u5);
        users.add(u6);
        // users.forEach(user -> userService.signUp(user));

        Boat b1 = new Boat("B1",4);
        Boat b2 = new Boat("B2",8);
        Boat b3 = new Boat("B3",4);

        List<Boat> boats = new ArrayList<>();
        boats.add(b1);
        boats.add(b2);
        boats.add(b3);
        // boats.forEach(boat -> boatService.createBoat(boat));

        LocalDateTime date = LocalDateTime.of(2022,05,20,10,00,00);
        Trip t1 = new Trip(date);
        // tripService.createTrip(t1);

        // create bookings
        Booking book1 = new Booking(2);
        Booking book2 = new Booking(8);
        Booking book3 = new Booking(9);
        Booking book4 = new Booking(5);
        Booking book5 = new Booking(1);
        Booking book6 = new Booking(2);

        book1.setUser(u1);
        book2.setUser(u2);
        book3.setUser(u3);
        book4.setUser(u4);
        book5.setUser(u5);
        book6.setUser(u6);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(book1);
        bookings.add(book2);
        bookings.add(book3);
        bookings.add(book4);
        bookings.add(book5);
        bookings.add(book6);

        bookings.forEach(booking -> booking.setTrip(t1));

        // given book1

        // Mockito.when(bookingRepository.findByUserIdAndTripId(null, null)).thenReturn(null);
        // Mockito.when(userService.getUser(null)).thenReturn(null);
        // Mockito.when(bookingRepository.findByTripId(null)).thenReturn(null);
        // Mockito.when(service.getRemainingSeatsByTripId(null)).thenReturn(16);
        // Mockito.when(underTest.getRemainingSeatsByTripId(null)).thenReturn(16);
        // Mockito.when(boatRepository.findAll()).thenReturn(boats);
        // Mockito.when(boatService.getTotalSeats()).thenReturn(16);
        // Mockito.when(underTest.getRemainingSeatsByTripId(null)).thenReturn(16);
        // verify(boatService, times(1)).getTotalSeats();

        // when then
        assertThatThrownBy(() -> underTest.createBooking(book1))
                .hasMessageContaining("FAIL. Posti terminati!");

    }
}