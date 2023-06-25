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

import com.mybootapp.main.exception.ResourceNotFoundException;
import com.mybootapp.main.model.Manager;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.model.User;
import com.mybootapp.main.service.ManagerService;
import com.mybootapp.main.service.MyUserService;

@RestController
@RequestMapping("/manager")
public class ManagerController {

	@Autowired
	private ManagerService managerService; 
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MyUserService userService; 
	@PostMapping("/add")
	public Manager postManager(@RequestBody Manager manager) {
		/*Read user info given as input and attach it to user object.  */
		User user = manager.getUser();
		user.setRole("MANAGER");
		
		/* Encode the password before saving in DB */
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		/* Save user in DB and fetch saved object */
		user = userService.insert(user);
		
		/* attach user to manager */
		manager.setUser(user);
		
		/* Save manager in DB */
		return managerService.insert(manager);
	}
	
	/* 
	 PATH: /Manager/all
	 Method: GET
	 RequestBody: None
	 response: List<Manager> 
	 PathVariable: None
	 */
	@GetMapping("/all")
	public List<Manager> getAllManagers() {
		List<Manager> list =  managerService.getAllManagers();
		return list; 
	}
	
	/* 
	 PATH: /Manager/one
	 Method: GET
	 RequestBody: None
	 response: Manager 
	 PathVariable: ID
	 */
	@GetMapping("/one/{id}") //this id is called as path variable
	public ResponseEntity<?> getManager(@PathVariable("id") int id) {
		Manager manager  = managerService.getmanager(id);
		if(manager == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		return ResponseEntity.status(HttpStatus.OK).body(manager); 
	}
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateManager(@PathVariable("id") int id, @RequestBody Manager newManager) {
		//Step 0 : validation for request body: newProduct
		if(newManager.getName() == null || !newManager.getName().trim().matches("[a-zA-Z0-9- *]+"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Name has to have valid format [a-zA-Z0-9- ]");
		
		if(newManager.getAddress() == null || newManager.getAddress().equals(""))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Address cannot be nullor blank");
		
		
		
		//Step 1: Validate the id given 
		Manager oldManager  = managerService.getmanager(id);
		if(oldManager == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		/* 2 techniques {old has id whereas new does not have id}
		 * 1. Transfer new values to old(that has id)
		 * 2. Transfer id from old to new.  
		 */
		newManager.setId(oldManager.getId());
		newManager = managerService.insert(newManager);
	    return ResponseEntity.status(HttpStatus.OK)
				.body(newManager);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteManager(@PathVariable("id") int id) {
		//Step 1: validate id
		Manager manager  = managerService.getmanager(id);
		if(manager == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		
		managerService.deleteProduct(manager);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body("manager deleted..");

	}
}