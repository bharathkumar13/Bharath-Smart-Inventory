package com.mybootapp.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybootapp.main.model.Godown;

public interface GodownRepository extends JpaRepository<Godown, Integer> {

}
