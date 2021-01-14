package it.barcaioli.webserver.customer;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerApi {

    private final CustomerService customerService;
    
    @Autowired
    public CustomerApi(CustomerService customerService){
    	this.customerService = customerService;
    }

    @GetMapping("/customers")
    public Iterable<Customer> getCustomers() {
        return customerService.getCustomers();
    }
    
    @GetMapping("/customers/{id}")
    public Customer getCustomer(@PathVariable Long id) {
    	return customerService.getCustomer(id);
    }
    
    @PostMapping("/customers/signup")
    public Customer signUp(@Valid @RequestBody Customer newCustomer) {
    	return customerService.signUp(newCustomer); 
    }
    
    @PostMapping("/customers/login")
    public Customer login(@Valid @RequestBody Customer customer) {
    	return customerService.login(customer);
    }
    
    @PostMapping("/customers/logout")
    public Customer logout(@Valid @RequestBody Customer customer) {
    	return customerService.logout(customer);
    }
    
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
    }
}