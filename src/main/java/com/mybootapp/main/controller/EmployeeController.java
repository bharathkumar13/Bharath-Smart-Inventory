package com.mybootapp.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybootapp.main.model.Employee;
import com.mybootapp.main.model.Manager;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.model.User;
import com.mybootapp.main.service.EmployeeService;
import com.mybootapp.main.service.ManagerService;
import com.mybootapp.main.service.MyUserService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private PasswordEncoder encoder; 
	
	@Autowired
	private MyUserService userService; 
	
	@Autowired
	private EmployeeService employeeService; 
	
	@Autowired
	private ManagerService managerService; 
	
	@PostMapping("/add/{managerId}")
	public ResponseEntity<?> addEmployee(@PathVariable("managerId") int managerId, 
			@RequestBody Employee employee) {
		/* validate managerId and fetch manager obj from DB */
		Manager manager  = managerService.getById(managerId);
		if(manager == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Manager ID invalid");
		
		/* attach manager to employee */
		employee.setManager(manager);
		
		/* Fetch the user from employee */
		User user = employee.getUser();
		
		/* Encode the password given as Plain text from UI */
		user.setPassword(encoder.encode(user.getPassword()));
		
		/* Set the role: EMPLOYEE */
		user.setRole("EMPLOYEE");
		
		/* Save the user in DB */
		user  = userService.insert(user);
		
		/* Attach user to employee and save employee */
		employee.setUser(user);
		employee =  employeeService.insert(employee);
		return ResponseEntity.status(HttpStatus.OK)
				.body(employee);
	}
	
	
	
	/* 
	 PATH: /employee/all
	 Method: GET
	 RequestBody: None
	 response: List<employee> 
	 PathVariable: None
	 */
	@GetMapping("/all")
	public List<Employee> getAllEmployees() {
		List<Employee> list =  employeeService.getAllEmployees();
		return list; 
	}
	
	/* 
	 PATH: /employee/one
	 Method: GET
	 RequestBody: None
	 response: employee 
	 PathVariable: ID
	 */
	@GetMapping("/one/{id}") //this id is called as path variable
	public ResponseEntity<?> getEmployee(@PathVariable("id") int id) {
		Employee employee  = employeeService.getEmployee(id);
		if(employee == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		return ResponseEntity.status(HttpStatus.OK).body(employee); 
	}
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable("id") int id, @RequestBody Employee newEmployee) {
		//Step 0 : validation for request body: newProduct
		if(newEmployee.getName() == null || !newEmployee.getName().trim().matches("[a-zA-Z0-9- *]+"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Name has to have valid format [a-zA-Z0-9- ]");
		
		if(newEmployee.getAddress() == null || newEmployee.getAddress().equals(""))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Address cannot be nullor blank");
		
		if(newEmployee.getJobTitle() == null || newEmployee.getJobTitle().equals(""))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("getJobTitle cannot be nullor blank");
		
		//Step 1: Validate the id given 
		Employee oldEmployee  = employeeService.getEmployee(id);
		if(oldEmployee == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		/* 2 techniques {old has id whereas new does not have id}
		 * 1. Transfer new values to old(that has id)
		 * 2. Transfer id from old to new.  
		 */
		newEmployee.setId(oldEmployee.getId());
		newEmployee = employeeService.insert(newEmployee);
	    return ResponseEntity.status(HttpStatus.OK)
				.body(newEmployee);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable("id") int id) {
		//Step 1: validate id
		Employee employee  = employeeService.getEmployee(id);
		if(employee == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		
		employeeService.deleteEmployee(employee);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body("employee deleted..");

	}
	
	
}