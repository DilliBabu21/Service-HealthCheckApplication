package com.tesla.security.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tesla.security.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
	
	Optional<Admin> findByUsername(String username);
	
	

}
