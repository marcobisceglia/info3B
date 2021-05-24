package it.barcaioli.webserver.user;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service // More specific @Component
public class UserService {

	private final UserRepository userRepository;
	private static final String ADMIN_EMAIL = "admin@gmail.com";

	@Autowired
	public UserService(UserRepository user) {
		this.userRepository = user;
	}

	public List<User> getUsers() {
		return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	public User getUser(Long id) {
		Optional<User> user = userRepository.findById(id);

		if (!user.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");

		return user.get();
	}

	private User getUserByEmail(String email) {
		List<User> users = userRepository.findByEmail(email);

		if (!users.isEmpty()) {
			return users.get(0);
		} else
			return null;
	}

	public User signUp(User newUser) {
		var user = getUserByEmail(newUser.getEmail());

		if (user != null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");

		// Hash the password for the first time, with a randomly-generated salt
		String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
		newUser.setPassword(hashed);
		if (newUser.getEmail().equals(ADMIN_EMAIL)) {
			newUser.setUserRole(UserRole.ADMIN);
		} else {
			newUser.setUserRole(UserRole.USER);
		}

		return userRepository.save(newUser);
	}

	public User login(User logginUser) {

		var user = getUserByEmail(logginUser.getEmail());

		if (user == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User email not found");

		if (BCrypt.checkpw(logginUser.getPassword(), user.getPassword())) {
			if (user.isLoggedIn()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already logged in");
			}
			user.setLoggedIn(true);
			return userRepository.save(user);
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password not correct");
		}
	}

	public void logout(User logginOutUser) {
		var user = getUserByEmail(logginOutUser.getEmail());

		if (user == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User email not found");

		if (user.isLoggedIn()) {
			user.setLoggedIn(false);
			userRepository.save(user);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not logged in");
		}
	}

	public void deleteUser(Long id) {
		Optional<User> userToDelete = userRepository.findById(id);

		if (!userToDelete.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user to delete");

		userRepository.delete(userToDelete.get());
	}
}
