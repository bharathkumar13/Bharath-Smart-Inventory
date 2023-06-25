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
import com.mybootapp.main.model.Product;
import com.mybootapp.main.model.Supplier;
import com.mybootapp.main.service.SupplierService;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
	
	@Autowired
	private SupplierService supplierService;
	
	@PostMapping("/add")
	public Supplier postSupplier(@RequestBody Supplier supplier) {
		return supplierService.insert(supplier);
	}
	
	
	/* 
	 PATH: /supplier/all
	 Method: GET
	 RequestBody: None
	 response: List<supplier> 
	 PathVariable: None
	 */
	@GetMapping("/all")
	public List<Supplier> getAllSuppliers() {
		List<Supplier> list =  supplierService.getAllSuppliers();
		return list; 
	}
	
	/* 
	 PATH: /supplier/one
	 Method: GET
	 RequestBody: None
	 response: Supplier 
	 PathVariable: ID
	 */
	@GetMapping("/one/{id}") //this id is called as path variable
	public ResponseEntity<?> getSupplier(@PathVariable("id") int id) {
		Supplier supplier  = supplierService.getsupplier(id);
		if(supplier == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		return ResponseEntity.status(HttpStatus.OK).body(supplier); 
	}
	
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateSupplier(@PathVariable("id") int id, @RequestBody Supplier newSupplier) {
		//Step 0 : validation for request body: newProduct
		if(newSupplier.getName() == null || !newSupplier.getName().trim().matches("[a-zA-Z0-9- *]+"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Name has to have valid format [a-zA-Z0-9- ]");
		
		if(newSupplier.getAddress() == null || newSupplier.getAddress().equals(""))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("address cannot be nullor blank");
		
		if(newSupplier.getCity() == null || newSupplier.getCity().equals(""))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("city cannot be nullor blank");
	
		
		//Step 1: Validate the id given 
		Supplier oldSupplier  = supplierService.getsupplier(id);
		if(oldSupplier == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		/* 2 techniques {old has id whereas new does not have id}
		 * 1. Transfer new values to old(that has id)
		 * 2. Transfer id from old to new.  
		 */
		newSupplier.setId(oldSupplier.getId());
		newSupplier = supplierService.insert(newSupplier);
	    return ResponseEntity.status(HttpStatus.OK)
				.body(newSupplier);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
		//Step 1: validate id
		Supplier supplier  = supplierService.getsupplier(id);
		if(supplier == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		
		supplierService.deleteProduct(supplier);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body("Product deleted..");

	}
	
	
	

}
