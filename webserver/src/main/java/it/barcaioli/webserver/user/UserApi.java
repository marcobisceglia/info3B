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

    private final UserService customerService;
    
    @Autowired
    public UserApi(UserService customerService){
    	this.customerService = customerService;
    }

    @GetMapping("/customers")
    public Iterable<User> getCustomers() {
        return customerService.getCustomers();
    }
    
    @GetMapping("/customers/{id}")
    public User getCustomer(@PathVariable Long id) {
    	return customerService.getCustomer(id);
    }
    
    @PostMapping("/customers/signup")
    public User signUp(@Valid @RequestBody User newCustomer) {
    	return customerService.signUp(newCustomer); 
    }
    
    @PostMapping("/customers/login")
    public User login(@Valid @RequestBody User customer) {
    	return customerService.login(customer);
    }
    
    @PostMapping("/customers/logout")
    public User logout(@Valid @RequestBody User customer) {
    	return customerService.logout(customer);
    }
    
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
    }
}