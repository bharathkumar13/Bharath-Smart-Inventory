package com.mybootapp.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybootapp.main.model.Customer;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService; 
	
	
	
	/* 
	 PATH: /customer/add
	 Method: POST
	 RequestBody: Customer customer
	 response: customer 
	 PathVariable: None
	 */
	@PostMapping("/add")
	public Customer postCustomer(@RequestBody Customer customer) {
		return customerService.insert(customer);
	}
	
	
	/* 
	 PATH: /customer/all
	 Method: GET
	 RequestBody: None
	 response: List<Customer> 
	 PathVariable: None
	 */
	@GetMapping("/all")
	public List<Customer> getAllCustomers(){
		List<Customer> list = customerService.getAllCustomers();
		return list;
	}
	
	
	/* 
	 PATH: /customer/one
	 Method: GET
	 RequestBody: None
	 response: customer 
	 PathVariable: ID
	 */
	
	@GetMapping("/one/{id}") //this id is called as path variable
	public ResponseEntity<?> getCustomer(@PathVariable("id") int id) {
		Customer customer  = customerService.getcustomer(id);
		if(customer == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		return ResponseEntity.status(HttpStatus.OK).body(customer); 
	}
	
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable("id") int id, @RequestBody Customer newCustomer) {
		//Step 0 : validation for request body: newProduct
		if(newCustomer.getName() == null || !newCustomer.getName().trim().matches("[a-zA-Z0-9- *]+"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("name has to have valid format [a-zA-Z0-9- ]");
		
		if(newCustomer.getAddress() == null || newCustomer.getAddress().equals(""))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Address cannot be nullor blank");
		
		if(newCustomer.getCity() == null || newCustomer.getCity().equals(""))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("city cannot be nullor blank");
		
		//Step 1: Validate the id given 
		Customer oldCustomer  = customerService.getcustomer(id);
		if(oldCustomer == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		/* 2 techniques {old has id whereas new does not have id}
		 * 1. Transfer new values to old(that has id)
		 * 2. Transfer id from old to new.  
		 */
		newCustomer.setId(oldCustomer.getId());
		newCustomer = customerService.insert(newCustomer);
	    return ResponseEntity.status(HttpStatus.OK)
				.body(newCustomer);
	}
	
	
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable("id") int id) {
		//Step 1: validate id
		Customer customer  = customerService.getcustomer(id);
		if(customer == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		
		customerService.deleteCustomer(customer);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body("Customer deleted..");

	}
	
	
}