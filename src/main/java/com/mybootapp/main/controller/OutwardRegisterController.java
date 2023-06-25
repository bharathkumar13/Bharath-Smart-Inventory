package com.mybootapp.main.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.mybootapp.main.dto.OutwardRegisterDto;
import com.mybootapp.main.model.Customer;
import com.mybootapp.main.model.CustomerProduct;
import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.OutwardRegister;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.service.CustomerProductService;
import com.mybootapp.main.service.CustomerService;
import com.mybootapp.main.service.GodownService;
import com.mybootapp.main.service.OutwardRegisterService;
import com.mybootapp.main.service.ProductService;

@RestController
@RequestMapping("/outwardregister")
public class OutwardRegisterController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private GodownService godownService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OutwardRegisterService outwardRegisterService;
	
	@Autowired
	private CustomerProductService customerProductService;

	
	@PostMapping("/add/{productId}/{godownId}")	
	public ResponseEntity<?> postInwardRegister(@RequestBody OutwardRegister outwardRegister, 
			                       @PathVariable("productId") int productId,
			                       @PathVariable("godownId")  int godownId)
			                        {

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
		
	
		
		//attach all objects to inward register
		
		
		
		outwardRegister.setProduct(product);
		outwardRegister.setGodown(godown);
		
		
		outwardRegister.setDateOfDelivery(LocalDate.now());
	    
		
		//save inward regisetr 
		
		outwardRegister = outwardRegisterService.insert(outwardRegister);
		
		return ResponseEntity.status(HttpStatus.OK).body(outwardRegister); 
	}
	
	@GetMapping("/all")
	public List<OutwardRegister> getAll() {
		return outwardRegisterService.getAll();
	}
	
	@GetMapping("/one/{id}") //this id is called as path variable
	public ResponseEntity<?> getOutwardRegister(@PathVariable("id") int id) {
		OutwardRegister outwardRegister  = outwardRegisterService.getoutwardRegister(id);
		if(outwardRegister == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		return ResponseEntity.status(HttpStatus.OK).body(outwardRegister); 
	}

	@PutMapping("/update/{productId}/{godownId}/{customerId}/{id}")
    public ResponseEntity<?> updateoutwardregister(@PathVariable("id") int id,
            @PathVariable("productId") int productId,
            @PathVariable("godownId") int godownId,
            @PathVariable("customerId") int customerId,

             @RequestBody OutwardRegister newoutwardregister)
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
         Customer customer=customerService.getById(customerId);
         if(customer==null)
         {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid id");
         }

      OutwardRegister oldoutwardregister=outwardRegisterService.getoutwardRegister(id);
      if(oldoutwardregister==null)
      {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid");

      }
      newoutwardregister.setGodown(godown);
         newoutwardregister.setProduct(product);
         newoutwardregister.setCustomer(customer);
      newoutwardregister.setId(oldoutwardregister.getId());

      newoutwardregister=outwardRegisterService.insert(newoutwardregister);
      return ResponseEntity.status(HttpStatus.OK).body(newoutwardregister);
     }
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteOutwardRegister(@PathVariable("id") int id) {
		//Step 1: validate id
		OutwardRegister outwardRegister  = outwardRegisterService.getoutwardRegister(id);
		if(outwardRegister == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		
		outwardRegisterService.deleteoutwardRegister(outwardRegister);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body("outward register deleted..");

	}
	
	@GetMapping("/report") // REPORT API 3:
	public List<OutwardRegisterDto> outwardReport() {
		List<OutwardRegister> list = outwardRegisterService.getAll();
		List<OutwardRegisterDto> listDto = new ArrayList<>();
		list.stream().forEach(entry -> {
			OutwardRegisterDto dto = new OutwardRegisterDto();
			dto.setProductTitle(entry.getProduct().getTitle());
			dto.setProductQuantity(entry.getQuantity());
			dto.setGodownLocation(entry.getGodown().getLocation());
			dto.setGodownManager(entry.getGodown().getManager().getName());
			dto.setReceiptNo(entry.getRecieptNo());
			
			listDto.add(dto);
		});
		
		return listDto;
	
	
}
	
	@GetMapping("/report/customer/{customerId}") //REPORT API 2:
    public ResponseEntity<?> outwardReportByCustomer(@PathVariable int customerId)  {
        List<CustomerProduct> list;
        list =  (List<CustomerProduct>) customerProductService.getByCustomerId(customerId);



        HashMap<String, Integer> map = new HashMap<>();
        list.stream().forEach(entry -> {
            if (map.containsKey(entry.getProduct().getTitle())) {
                map.put(entry.getProduct().getTitle(), map.get(entry.getProduct().getTitle()) + entry.getQuantityPuchased());
            }
            else {
                map.put(entry.getProduct().getTitle(), entry.getQuantityPuchased());                
            }
        });



        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
