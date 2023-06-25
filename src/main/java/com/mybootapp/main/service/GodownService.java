package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.repository.GodownRepository;

@Service
public class GodownService {
	
	@Autowired
	private GodownRepository godownRepository;

	public Godown insert(Godown godown) {
		return godownRepository.save(godown);
	}

	public List<Godown> getAll() {
		return godownRepository.findAll();
	}

	public Godown getById(int godownId) {
		Optional<Godown> optional = godownRepository.findById(godownId);
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	public Godown getgodown(int id) {
		Optional<Godown> optional = godownRepository.findById(id);
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	public void deleteGodown(Godown godown) {
		godownRepository.delete(godown);
		
	}

}
