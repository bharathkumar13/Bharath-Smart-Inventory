package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Customer;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository; 
	
	public Customer insert(Customer customer) {
		 
		return customerRepository.save(customer);
	}

	public Customer getById(int customerId) {
		Optional<Customer> optional= customerRepository.findById(customerId);
		if(!optional.isPresent()) {
			return null; 
		}
		return optional.get();
	}

	public List<Customer> getAllCustomers() {
		
		return customerRepository.findAll();
	}

	public Customer getcustomer(int id) {
		Optional<Customer> optional = customerRepository.findById(id);
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	public void deleteCustomer(Customer customer) {
		customerRepository.delete(customer);
		
	}

}