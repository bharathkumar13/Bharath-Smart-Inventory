package com.mybootapp.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybootapp.main.model.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {

}
