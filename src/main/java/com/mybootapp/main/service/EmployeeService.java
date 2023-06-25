package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Employee;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository; 
	public Employee insert(Employee employee) {
 		return employeeRepository.save(employee);
	}
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
		
	}
	public Employee getEmployee(int id) {
		Optional<Employee> optional = employeeRepository.findById(id);
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}
	public void deleteEmployee(Employee employee) {
		
		employeeRepository.delete(employee);
	}

}