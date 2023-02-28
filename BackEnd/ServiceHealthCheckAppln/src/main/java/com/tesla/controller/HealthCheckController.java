package com.tesla.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.model.Services;
import com.tesla.security.model.AuthenticationRequest;
import com.tesla.security.model.AuthenticationResponse;
import com.tesla.security.service.AdminService;
import com.tesla.service.HealthCheckService;
import com.tesla.service.ServicesHealthService;

@RestController
@CrossOrigin("*")
@RequestMapping("/healthcheck")
public class HealthCheckController {

	private HealthCheckService healthCheckService;
	private ServicesHealthService servicesHealthService;
	private AdminService adminService;
	
	
	public HealthCheckController(HealthCheckService healthCheckService, ServicesHealthService servicesHealthService, AdminService adminService) {
		this.adminService = adminService;
		this.healthCheckService = healthCheckService;
		this.servicesHealthService = servicesHealthService;
	}
	
	

	@PostMapping("service/add")
	public Services addService(@RequestBody Services service) {
		return servicesHealthService.addService(service);
	}

//	@GetMapping("/check")
//	public ResponseEntity<Services> checkHealth(@RequestParam("url") String url,@RequestParam HttpMethod httpMethod, @RequestBody(required = false) String payload) {
//		Services service = new Services();
//		service.setUrl(url);
//		service.setHttpMethod(httpMethod);
//		 service.setPayload(payload);
//		Services result = healthCheckService2.checkHealth(service, url);
//
//		return new ResponseEntity<Services>(result, HttpStatus.OK);
//	}

	@GetMapping("/service/check/{id}")
	public ResponseEntity<Services> checkHealth(@PathVariable int id) throws Exception {
		Services service = healthCheckService.checkHealth(id);
		return new ResponseEntity<>(service, HttpStatus.OK);
	}

	@GetMapping("/service/checkall")
	public void checkAllServices() {
		healthCheckService.checkAllServices();
		

	}

	@GetMapping("/service/{id}")
	public Services viewService(@PathVariable int id) {
		return servicesHealthService.viewServiceById(id);
	}

	@PostMapping("/service/send-email")
	public ResponseEntity<String> sendEmail() {
		healthCheckService.sendEmail();
		return new ResponseEntity<>("Emails sent successfully", HttpStatus.OK);
	}

	@GetMapping("/services")
	public List<Services> findAll() {
		return servicesHealthService.findAllServices();

	}

	@PutMapping("/service/{id}")
	public String updateService(@RequestBody Services service, @PathVariable int id) {
		return servicesHealthService.updateService(service, id);
	}

	@DeleteMapping("/service/{id}")
	public String deleteService(@PathVariable int id) {
		return servicesHealthService.deleteService(id);
	}



	
}
