package com.tesla.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tesla.model.Services;

@Repository
public interface ServicesHealthRepository extends JpaRepository<Services, Integer>{

	Services findByUrl(String url);

}
