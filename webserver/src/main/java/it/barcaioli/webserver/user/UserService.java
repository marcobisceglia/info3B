package it.barcaioli.webserver.user;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service // A @Component more specific
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository user) {
		this.userRepository = user;
	}

	public List<User> getUsers() {
		// findAll() it's auto generated by SpringBoot
		return userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
	}

	public User getUser(Long id) {
		// findById() it's auto generated by SpringBoot
		Optional<User> user = userRepository.findById(id);

		if (!user.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");

		return user.get();
	}

	private User getUserByEmail(String email) {
		Iterable<User> users = userRepository.findAll();

		for (User user : users) {
			if (user.getEmail().equalsIgnoreCase(email)) {
				return user;
			}
		}
		return null;
	}

	public User signUp(User newUser) {
		User user = getUserByEmail(newUser.getEmail());

		if (user != null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");

		// Hash the password for the first time, with a randomly-generated salt
		String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
		newUser.setPassword(hashed);

		return userRepository.save(newUser);
	}

	public User login(User logginUser) {

		User user = getUserByEmail(logginUser.getEmail());

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

	public User logout(User logginOutUser) {
		User user = getUserByEmail(logginOutUser.getEmail());

		if (user == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User email not found");

		if (user.isLoggedIn()) {
			user.setLoggedIn(false);
			return userRepository.save(user);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not logged in");
		}
	}

	public void deleteUser(Long id) {
		Optional<User> userToDelete = userRepository.findById(id);

		if (!userToDelete.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user to delete");

		// delete() it's auto generated by SpringBoot
		userRepository.delete(userToDelete.get());
	}
}
