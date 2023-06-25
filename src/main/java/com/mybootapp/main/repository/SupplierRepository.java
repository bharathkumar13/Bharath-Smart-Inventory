package com.mybootapp.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybootapp.main.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Integer>{

}
