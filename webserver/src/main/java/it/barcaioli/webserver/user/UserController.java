package it.barcaioli.webserver.user;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public Iterable<User> getUsers() {
		return userService.getUsers();
	}

	@GetMapping(path = "{userId}")
	public User getUser(@PathVariable Long userId) {
		return userService.getUser(userId);
	}

	@PostMapping(path = "signup")
	public User signUp(@Valid @RequestBody User user) {
		return userService.signUp(user);
	}

	@PostMapping(path = "login")
	public User login(@Valid @RequestBody User user) {
		return userService.login(user);
	}

	@PostMapping(path = "logout")
	public User logout(@Valid @RequestBody User user) {
		return userService.logout(user);
	}

	@DeleteMapping(path = "{userId}")
	public void deleteUser(@PathVariable("userId") Long userId) {
		userService.deleteUser(userId);
	}
}
