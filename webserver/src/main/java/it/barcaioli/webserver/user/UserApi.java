package it.barcaioli.webserver.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi {

    private final UserService userService;
    
    @Autowired
    public UserApi(UserService userService){
    	this.userService = userService;
    }

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return userService.getUsers();
    }
    
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
    	return userService.getUser(id);
    }
    
    @PostMapping("/users/signup")
    public User signUp(@Valid @RequestBody User newUser) {
    	return userService.signUp(newUser); 
    }
    
    @PostMapping("/users/login")
    public User login(@Valid @RequestBody User user) {
    	return userService.login(user);
    }
    
    @PostMapping("/users/logout")
    public User logout(@Valid @RequestBody User user) {
    	return userService.logout(user);
    }
    
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}