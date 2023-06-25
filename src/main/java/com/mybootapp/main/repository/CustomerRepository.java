package com.mybootapp.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybootapp.main.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}