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

import com.mybootapp.main.exception.ResourceNotFoundException;
import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.Manager;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.service.GodownService;
import com.mybootapp.main.service.ManagerService;



@RestController
@RequestMapping("/godown")
public class GodownController {

	@Autowired
	private GodownService godownService;
	
	@Autowired
	private ManagerService managerService; 
	
	@PostMapping("/add/{managerID}")
	public ResponseEntity<?> insertGodown(@PathVariable("managerID") int managerID, 
			@RequestBody Godown godown) {
		//Step 0: validation, if needed for Request body, is done is ProductController in PUT api. 
		
		/* Step 1: Validate and fetch Manager from managerId */
		Manager manager = managerService.getById(managerID);
		if(manager == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid Manager ID"); 
		
		/* Step 2: attach manager to godown object */
			godown.setManager(manager);
			
		/* Step 3: save godown object */
		godown = godownService.insert(godown);
		
		return ResponseEntity.status(HttpStatus.OK).body(godown);
	}
	
	@GetMapping("/all")
	public List<Godown> getAllGodowns(){
		return godownService.getAll();
		
	}
	
	@GetMapping("/one/{id}") //this id is called as path variable
	public ResponseEntity<?> getGodown(@PathVariable("id") int id) {
		Godown godown  = godownService.getgodown(id);
		if(godown == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		return ResponseEntity.status(HttpStatus.OK).body(godown); 
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updategodown(@PathVariable("id") int id, @RequestBody Godown newGodown) {
		//Step 0 : validation for request body: newProduct
		if(newGodown.getLocation() == null || !newGodown.getLocation().trim().matches("[a-zA-Z0-9- *]+"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Location has to have valid format [a-zA-Z0-9- ]");
		
		if(newGodown.getCapacityInQuintals() == 0 )
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("getCapacityInQuintals must have a value other than 0");
		
		
		
		//Step 1: Validate the id given 
		Godown oldGodown  = godownService.getgodown(id);
		if(oldGodown == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		/* 2 techniques {old has id whereas new does not have id}
		 * 1. Transfer new values to old(that has id)
		 * 2. Transfer id from old to new.  
		 */
		newGodown.setId(oldGodown.getId());
		newGodown = godownService.insert(newGodown);
	    return ResponseEntity.status(HttpStatus.OK)
				.body(newGodown);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteGodown(@PathVariable("id") int id) {
		//Step 1: validate id
		Godown godown  = godownService.getgodown(id);
		if(godown == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		
		godownService.deleteGodown(godown);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body("godown deleted..");

	}
	
	
	// Report API 4:
	@GetMapping("/report")
	public List<Godown> godownReport() {
		return godownService.getAll();
	}
	
	
	
}