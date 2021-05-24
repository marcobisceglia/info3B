package it.barcaioli.webserver.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import it.barcaioli.webserver.booking.Booking;

@RestController
@RequestMapping(path = "users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	private User dtoToEntity(UserDto userDto) {
		var user = new User();
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail().toLowerCase());
		user.setPassword(userDto.getPassword());
		user.setLicense(userDto.getLicense());
		return user;
	}

	@GetMapping
	public Iterable<User> getUsers() {
		return userService.getUsers();
	}

	@GetMapping(path = "{userId}")
	public User getUser(@PathVariable Long userId) {
		return userService.getUser(userId);
	}

	@GetMapping(path = "{userId}/bookings")
	public List<Booking> getUserBookings(@PathVariable Long userId,
			@RequestParam(required = false, name = "trip") Long tripId) {
		if (tripId == null)
			return userService.getUser(userId).getBookings();
		else
			return userService.getUser(userId).getBookings().stream().filter(b -> b.getTrip().getId().equals(tripId))
					.collect(Collectors.toList());

	}

	@PostMapping(path = "signup")
	public User signUp(@Valid @RequestBody UserDto userDto) {
		var user = dtoToEntity(userDto);
		return userService.signUp(user);
	}

	@PostMapping(path = "login")
	public User login(@Valid @RequestBody UserDto userDto) {
		var user = dtoToEntity(userDto);
		return userService.login(user);
	}

	@PostMapping(path = "logout")
	public void logout(@Valid @RequestBody UserDto userDto) {
		var user = dtoToEntity(userDto);
		userService.logout(user);
	}

	@DeleteMapping(path = "{userId}")
	public void deleteUser(@PathVariable("userId") Long userId) {
		userService.deleteUser(userId);
	}
}
