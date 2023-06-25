package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Manager;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.repository.ManagerRepository;

@Service
public class ManagerService {
	
	@Autowired
	private ManagerRepository managerRepository;

	

	public Manager insert(Manager manager) {
		return  managerRepository.save(manager);
	}



	public Manager getById(int managerID) {
		Optional<Manager> optional = managerRepository.findById(managerID);
		
		if(!optional.isPresent())
			return null;
		
		return optional.get(); //returns manager
	}



	public List<Manager> getAllManagers() {
		return managerRepository.findAll();
	}



	public Manager getmanager(int id) {
		Optional<Manager> optional = managerRepository.findById(id);
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}



	public void deleteProduct(Manager manager) {
		managerRepository.delete(manager);
		
	}



	
}
