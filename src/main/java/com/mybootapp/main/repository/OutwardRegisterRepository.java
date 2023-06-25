package com.mybootapp.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybootapp.main.model.OutwardRegister;

public interface OutwardRegisterRepository extends JpaRepository<OutwardRegister, Integer> {

}