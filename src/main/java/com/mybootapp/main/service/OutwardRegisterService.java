package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.OutwardRegister;
import com.mybootapp.main.repository.OutwardRegisterRepository;


@Service
public class OutwardRegisterService {	
	
	@Autowired
	private OutwardRegisterRepository outwardRegisterRepository;
	

	public OutwardRegister insert(OutwardRegister outwardRegister) {
		return outwardRegisterRepository.save(outwardRegister);
	}


	public List<OutwardRegister> getAll() {
		return outwardRegisterRepository.findAll();
	}


	public OutwardRegister getoutwardRegister(int id) {
		Optional<OutwardRegister> optional = outwardRegisterRepository.findById(id);
		if (!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}


	public void deleteoutwardRegister(OutwardRegister outwardRegister) {
		outwardRegisterRepository.delete(outwardRegister);
		
	}

}
