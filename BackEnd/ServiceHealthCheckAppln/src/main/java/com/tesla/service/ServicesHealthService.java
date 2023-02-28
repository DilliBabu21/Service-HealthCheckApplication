package com.tesla.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.tesla.config.AuthClient;
import com.tesla.model.Services;
import com.tesla.repo.ServicesHealthRepository;

@Service
public class ServicesHealthService {
	
	private ServicesHealthRepository servicesHealthRepository;

	@Autowired
	public ServicesHealthService(ServicesHealthRepository servicesHealthRepository, AuthClient authClient) {
		this.servicesHealthRepository = servicesHealthRepository;
		this.authClient = authClient;
	}
	
	
	private final AuthClient authClient;

	
	
	   public Services addService(Services service) {
		   service.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		return servicesHealthRepository.save(service);
	        
	    }
	   
	   public Services viewServiceById(int id) {
		return servicesHealthRepository.findById(id).orElse(null);  
	   }

	    public String updateService(Services service,int id) {
	    	 Services existingService = servicesHealthRepository.findById(id).orElseThrow();
	    	    existingService.setService_name(service.getService_name());
	    	    existingService.setUrl(service.getUrl());
	    	    existingService.setEmail(service.getEmail());
	    	    existingService.setHttpMethod(service.getHttpMethod());
	    	    existingService.setPayload(service.getPayload());
	    	    existingService.setStatus(service.getStatus());
	    	    servicesHealthRepository.save(existingService);
	    	    return "Updated Sucessfully";
	       
	    }

	    public String deleteService(int id) {
	    	servicesHealthRepository.deleteById(id);
			return "Service Deleted";
	        
	    }

	    public List<Services> findAllServices() {
	    	
	    	return servicesHealthRepository.findAll();
	    }
	
	
	
	    public Authentication authenticate() {
	        return authClient.authenticate();
	    }
	    




}
