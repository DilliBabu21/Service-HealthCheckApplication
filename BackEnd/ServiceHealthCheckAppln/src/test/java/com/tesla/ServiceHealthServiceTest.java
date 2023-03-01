package com.tesla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tesla.model.ServiceStatus;
import com.tesla.model.Services;
import com.tesla.service.ServicesHealthService;

@RunWith(SpringRunner.class)
	@SpringBootTest
	public class ServiceHealthServiceTest {

	    @Autowired
	    private ServicesHealthService servicesHealthService;

	    @Test
	    public void testAddService() {
	        Services service = new Services();
	        service.setService_name("Test Service");
	        service.setUrl("https://test-service.com");
	        service.setEmail("test@test-service.com");
	        service.setHttpMethod("GET");
	        service.setPayload("");
	        service.setStatus(ServiceStatus.UNKNOWN);

	        Services savedService = servicesHealthService.addService(service);

	        assertNotNull(savedService.getId());
	        assertEquals("Test Service", savedService.getService_name());
	        assertEquals("https://test-service.com", savedService.getUrl());
	        assertEquals("test@test-service.com", savedService.getEmail());
	        assertEquals("GET", savedService.getHttpMethod());
	        assertEquals(service.getPayload(), savedService.getPayload());
	        assertEquals(service.getStatus(), savedService.getStatus());
	    }
	    
	    @Test
	    public void testViewServiceById() {
	        Services service = new Services();
	        service.setService_name("Test Service");
	        service.setUrl("https://test-service.com");
	        service.setEmail("test@test-service.com");
	        service.setHttpMethod("GET");
	        service.setPayload("");
	        service.setStatus(ServiceStatus.UNKNOWN);

	        Services savedService = servicesHealthService.addService(service);

	        Services retrievedService = servicesHealthService.viewServiceById(savedService.getId());

	        assertNotNull(retrievedService);
	        assertEquals(savedService.getId(), retrievedService.getId());
	        assertEquals(savedService.getService_name(), retrievedService.getService_name());
	        assertEquals(savedService.getUrl(), retrievedService.getUrl());
	        assertEquals(savedService.getEmail(), retrievedService.getEmail());
	        assertEquals(savedService.getHttpMethod(), retrievedService.getHttpMethod());
	        assertEquals(savedService.getPayload(), retrievedService.getPayload());
	        assertEquals(savedService.getStatus(), retrievedService.getStatus());
	    }
}
