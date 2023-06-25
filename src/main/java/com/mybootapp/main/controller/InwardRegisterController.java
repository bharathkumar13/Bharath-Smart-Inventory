package com.mybootapp.main.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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

import com.mybootapp.main.dto.InwardRegisterDto;
import com.mybootapp.main.dto.InwardRegisterSupplierDto;
import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.InwardRegister;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.model.Supplier;
import com.mybootapp.main.service.GodownService;
import com.mybootapp.main.service.InwardRegisterService;
import com.mybootapp.main.service.ProductService;
import com.mybootapp.main.service.SupplierService;

@RestController
@RequestMapping("/inwardregister")
public class InwardRegisterController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private GodownService godownService;
	
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private InwardRegisterService inwadrRegisterService;
	
@PostMapping("/add/{productId}/{godownId}/{supplierId}")	
public ResponseEntity<?> postInwardRegister(@RequestBody InwardRegister inwardRegister, 
		                       @PathVariable("productId") int productId,
		                       @PathVariable("godownId")  int godownId,
		                       @PathVariable("supplierId") int supplierId) {

	// validate Ids and fetch Objects
	
	//product
	
	Product product = productService.getById(productId);
	
	if(product==null) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Invalid product ID given");
	}
	
	
Godown godown = godownService.getById(godownId);
	
	if(godown==null) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Invalid godown ID given");
	}
	
Supplier supplier = supplierService.getById(supplierId);
	
	if(supplier==null) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Invalid ID given");
	}
	
	//attach all objects to inward register
	
	
	inwardRegister.setProduct(product);
	inwardRegister.setGodown(godown);
	inwardRegister.setSupplier(supplier);
	
	inwardRegister.setDateOfSupply(LocalDate.now());
    
	
	
	//save inward regisetr 
	
	inwardRegister = inwadrRegisterService.insert(inwardRegister);
	
	return ResponseEntity.status(HttpStatus.OK).body(inwardRegister); 
}

@GetMapping("/all")
public List<InwardRegister> getAll() {
	return inwadrRegisterService.getAll();
}

@GetMapping("/one/{id}") //this id is called as path variable
public ResponseEntity<?> getInwardRegister(@PathVariable("id") int id) {
	InwardRegister inwardRegister  = inwadrRegisterService.getinwardRegister(id);
	if(inwardRegister == null) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Invalid ID given");
	}
	return ResponseEntity.status(HttpStatus.OK).body(inwardRegister); 
}


@PutMapping("/update/{productId}/{godownId}/{supplierId}/{id}")
public ResponseEntity<?> updateinwardregister(@PathVariable("id") int id,
        @PathVariable("productId") int productId,
        @PathVariable("godownId") int godownId,
        @PathVariable("supplierId") int supplierId,

         @RequestBody InwardRegister newinwardregister)
{
     Product product=productService.getById(productId);
     if(product==null)
     {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid id");
     }
     Godown godown=godownService.getById(godownId);
     if(godown==null)
     {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid id");
     }
     Supplier supplier=supplierService.getById(supplierId);
     if(supplier==null)
     {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid id");
     }
     InwardRegister oldinwardregister=inwadrRegisterService.getinwardRegister(id);
     if(oldinwardregister==null)
     {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid");

     }
     newinwardregister.setGodown(godown);
        newinwardregister.setProduct(product);
        newinwardregister.setSupplier(supplier);
     newinwardregister.setId(oldinwardregister.getId());

     newinwardregister=inwadrRegisterService.insert(newinwardregister);
     return ResponseEntity.status(HttpStatus.OK).body(newinwardregister);
    }


@DeleteMapping("/delete/{id}")
public ResponseEntity<?> deleteInwardRegister(@PathVariable("id") int id) {
	//Step 1: validate id
	InwardRegister inwardRegister  = inwadrRegisterService.getinwardRegister(id);
	if(inwardRegister == null) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Invalid ID given");
	}
	
	inwadrRegisterService.deleteinwardRegister(inwardRegister);
	
	return ResponseEntity.status(HttpStatus.OK)
			.body("Product deleted..");

}


@GetMapping("/report")
public List<?> getReport(){
	List<InwardRegister> list = inwadrRegisterService.getAll();
	List<InwardRegisterDto> listdto = new ArrayList<>();
	list.stream().forEach(entry -> {
		InwardRegisterDto dto = new InwardRegisterDto(); //100X 200X
		 dto.setProductTitle(entry.getProduct().getTitle());
		 dto.setProductQuantity(entry.getQuantity());
		 dto.setGodownLocation(entry.getGodown().getLocation());
		 dto.setGodownManager(entry.getGodown().getManager().getName());
		 dto.setSupplierName(entry.getSupplier().getName());
		 dto.setSupplierCity(entry.getSupplier().getCity());
		 listdto.add(dto);
	});
	
	return listdto;
}


@GetMapping("/report/supplier/{supplierId}") // REPORT API 1:
public ResponseEntity<?> inwardReportBySupplier(@PathVariable int supplierId) {
	List<InwardRegister> list;
	
		list = inwadrRegisterService.getBySupplierId(supplierId);	
	
	List<InwardRegisterSupplierDto> listDto = new ArrayList<>();
	list.stream().forEach(entry -> {
		InwardRegisterSupplierDto dto = new InwardRegisterSupplierDto();
		dto.setProductTitle(entry.getProduct().getTitle());
		dto.setProductQuantity(entry.getQuantity());
		dto.setProductPrice(entry.getProduct().getPrice());
		dto.setSupplierName(entry.getSupplier().getName());
		dto.setSupplierCity(entry.getSupplier().getCity());
		
		listDto.add(dto);
	});
	
	return ResponseEntity.status(HttpStatus.OK).body(listDto);
}

}
