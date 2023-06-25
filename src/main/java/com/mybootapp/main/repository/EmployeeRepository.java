package com.mybootapp.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybootapp.main.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer>{

}