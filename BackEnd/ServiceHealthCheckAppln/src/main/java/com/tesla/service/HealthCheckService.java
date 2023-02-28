package com.tesla.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tesla.model.ServiceStatus;
import com.tesla.model.Services;
import com.tesla.repo.ServicesHealthRepository;

@Service
public class HealthCheckService {

	@Autowired
	private ServicesHealthRepository servicesHealthRepository;
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private JavaMailSender javaMailSender;

	
	@Scheduled(fixedRate = 360000) // run every minute
	public void scheduleHealthCheck() {
		List<Services> servicesList = servicesHealthRepository.findAll();
		for (Services service : servicesList) {
			CompletableFuture.runAsync(() -> {
				String httpMethod = service.getHttpMethod();
				if (httpMethod.equalsIgnoreCase("POST") || httpMethod.equalsIgnoreCase("GET")) {
					Services updatedService = checkHealth(service, service.getUrl(), service.getHttpMethod(),
							service.getPayload());
					updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
					servicesHealthRepository.save(updatedService);
				} else if (httpMethod.equalsIgnoreCase("PUT")) {
				    try {
				        Thread.sleep(4000); // wait 4 seconds before checking PUT requests
				        Services updatedService = checkHealth(service, service.getUrl(), service.getHttpMethod(), service.getPayload());
				        updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
				        servicesHealthRepository.save(updatedService);
				    } catch (InterruptedException e) {
				        // log or handle the exception
				    }
				} else if (httpMethod.equalsIgnoreCase("DELETE")) {
				    try {
				        Thread.sleep(5000); // wait 5 seconds before checking DELETE requests
				        Services updatedService = checkHealth(service, service.getUrl(), service.getHttpMethod(), service.getPayload());
				        updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
				        servicesHealthRepository.save(updatedService);
				    } catch (InterruptedException e) {
				        // log or handle the exception
				    }
				}

			});
		}
	}
	
	
	//Health Check Method for single service
	public void checkAllServices() {
		List<Services> servicesList = servicesHealthRepository.findAll();
		for (Services service : servicesList) {
			CompletableFuture.runAsync(() -> {
				String httpMethod = service.getHttpMethod();
				if (httpMethod.equalsIgnoreCase("POST") || httpMethod.equalsIgnoreCase("GET")) {
					Services updatedService = checkHealth(service, service.getUrl(), service.getHttpMethod(),
							service.getPayload());
					updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
					servicesHealthRepository.save(updatedService);
				} else if (httpMethod.equalsIgnoreCase("PUT")) {
				    try {
				        Thread.sleep(2000); // wait 4 seconds before checking PUT requests
				        Services updatedService = checkHealth(service, service.getUrl(), service.getHttpMethod(), service.getPayload());
				        updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
				        servicesHealthRepository.save(updatedService);
				    } catch (InterruptedException e) {
				        // log or handle the exception
				    }
				} else if (httpMethod.equalsIgnoreCase("DELETE")) {
				    try {
				        Thread.sleep(3000); // wait 5 seconds before checking DELETE requests
				        Services updatedService = checkHealth(service, service.getUrl(), service.getHttpMethod(), service.getPayload());
				        updatedService.setCheckedAt(new Timestamp(System.currentTimeMillis()));
				        servicesHealthRepository.save(updatedService);
				    } catch (InterruptedException e) {
				        // log or handle the exception
				    }
				}

			});
		}
		
	
	}


	//Health Check Method for single service
	public Services checkHealth(int id) throws Exception {
		Optional<Services> serviceOptional = servicesHealthRepository.findById(id);
		if (serviceOptional.isPresent()) {
			Services service = serviceOptional.get();
			String url = service.getUrl();
			String httpMethod = service.getHttpMethod();
			Object payload = service.getPayload();
			// call the checkHealth method to update the service status
			Services updatedService = checkHealth(service, url, httpMethod, payload);
			// save the updated service status to the database
			servicesHealthRepository.save(updatedService);
			return updatedService;
		} else {
			throw new Exception("Service not found with id " + id);
		}
	}

	//Health Check Method
	public Services checkHealth(Services service, String url, String httpMethod, Object payload) {
		Services existingService = servicesHealthRepository.findByUrl(service.getUrl());
		long startTime = System.currentTimeMillis();
		try {
			ResponseEntity<String> response;
			httpMethod = service.getHttpMethod();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			payload = service.getPayload();
			if (payload instanceof String) {
				String payloadStr = (String) payload;
				try {
					payload = Integer.parseInt(payloadStr);
				} catch (NumberFormatException e) {
					try {
						payload = Long.parseLong(payloadStr);
					} catch (NumberFormatException ex) {
						try {
							payload = Double.parseDouble(payloadStr);
						} catch (NumberFormatException exc) {
							// if payload is not numeric, do nothing
						}
					}
				}
			}

			HttpEntity<String> requestEntity = new HttpEntity<>(service.getPayload(), headers);
			if (httpMethod.equals("POST")) {
				response = restTemplate.exchange(service.getUrl(), HttpMethod.POST, requestEntity, String.class);
			} else if (httpMethod.equals("PUT")) {
				response = restTemplate.exchange(service.getUrl(), HttpMethod.PUT, requestEntity, String.class);
			} else if (httpMethod.equals("DELETE")) {
				response = restTemplate.exchange(service.getUrl(), HttpMethod.DELETE, requestEntity, String.class);
			} else {
				response = restTemplate.exchange(service.getUrl(), HttpMethod.GET, requestEntity, String.class);
			}

			long endTime = System.currentTimeMillis();
			long timeTaken = endTime - startTime;

			if (response.getStatusCode().is2xxSuccessful()) {
				if (existingService != null && timeTaken > 10000) { // 10 seconds
					existingService.setStatus(ServiceStatus.SLOW);
				} else {
					if (existingService != null) {
						existingService.setStatus(ServiceStatus.UP);
					} else {
						service.setStatus(ServiceStatus.UP);
					}
				}
				servicesHealthRepository.save(existingService != null ? existingService : service);
				return existingService != null ? existingService : service;
			} else {
				if (existingService != null) {
					existingService.setStatus(ServiceStatus.DOWN);
					servicesHealthRepository.save(existingService);
					// sendEmail(existingService);
					return existingService;
				} else {
					service.setStatus(ServiceStatus.DOWN);
					servicesHealthRepository.save(service);
					// sendEmail(service);
					return service;
				}
			}
		} catch (Exception ex) {
			if (existingService != null) {
				existingService.setStatus(ServiceStatus.DOWN);
				servicesHealthRepository.save(existingService);
				// sendEmail(existingService);
				return existingService;
			} else {
				service.setStatus(ServiceStatus.DOWN);
				servicesHealthRepository.save(service);
				// sendEmail(service);
				return service;
			}
		}
	}
	
	
	//Mail Method
	public void sendEmail() {
		
		List<Services> services = servicesHealthRepository.findAll();
		for (Services service : services) {
		String status1 = service.getStatus().name();
		
		if(status1.equals("DOWN")) {
			String ownerEmail = service.getEmail();
			String serviceUrl = service.getUrl();
			String serviceName = service.getService_name();
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(ownerEmail);
			mailMessage.setSubject("Service Health Alert");
			mailMessage.setText("Service: " + serviceName + " with URL: " + serviceUrl + " is currently down");
			javaMailSender.send(mailMessage);
			
		}
		}
	}

}

