package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.model.Supplier;
import com.mybootapp.main.repository.SupplierRepository;

@Service
public class SupplierService {
	@Autowired
	private SupplierRepository supplierRepository;

	public Supplier insert(Supplier supplier) {
		return supplierRepository.save(supplier);
		
	}

	public Supplier getById(int supplierId) {
		Optional<Supplier> optional = supplierRepository.findById(supplierId);
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	public List<Supplier> getAllSuppliers() {
		return supplierRepository.findAll();
		
	}

	public Supplier getsupplier(int id) {
		Optional<Supplier> optional = supplierRepository.findById(id);
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	public void deleteProduct(Supplier supplier) {
		supplierRepository.delete(supplier);
		
	}
	
	
	

}
