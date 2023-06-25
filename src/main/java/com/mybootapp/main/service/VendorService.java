package com.mybootapp.main.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Vendor;
import com.mybootapp.main.repository.VendorRepository;

@Service
public class VendorService {

	@Autowired
	private VendorRepository vendorRepository; 
	
	public Vendor insert(Vendor vendor) {
		 return vendorRepository.save(vendor);
	}

	public Vendor getById(int vendorId) {
		 
		Optional<Vendor> optional = vendorRepository.findById(vendorId);
		if(!optional.isPresent())
			return null;
		return optional.get();
	}

}