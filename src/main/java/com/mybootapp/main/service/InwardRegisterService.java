package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.InwardRegister;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.repository.InwardRegisterRepository;

@Service
public class InwardRegisterService {

	@Autowired
	private InwardRegisterRepository inwardRegisterRepository;
	
	public InwardRegister insert(InwardRegister inwardRegister) {
		 
		return inwardRegisterRepository.save(inwardRegister);
	}

	public boolean checkQuantity(int productId, int quantityPuchased) {
		InwardRegister inwardRegister = inwardRegisterRepository
				.checkQuantity(productId,quantityPuchased);
		if(inwardRegister == null)
			return false;
		return true;
	}

	public List<InwardRegister> getAll() {
		
		return inwardRegisterRepository.findAll();
	}

	public InwardRegister getinwardRegister(int id) {
		Optional<InwardRegister> optional = inwardRegisterRepository.findById(id);
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	public void deleteinwardRegister(InwardRegister inwardRegister) {
		inwardRegisterRepository.delete(inwardRegister);
		
	}

	public List<InwardRegister> getBySupplierId(int supplierId)  {
		List<InwardRegister> inwardRegisterList = inwardRegisterRepository.findAllBySupplierId(supplierId);
			
		return inwardRegisterList;
	}

	

}