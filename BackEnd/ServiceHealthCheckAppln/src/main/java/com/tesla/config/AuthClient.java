package com.tesla.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-microservice", url = "${auth-microservice.url}")
public interface AuthClient {
	
	  @GetMapping("/api/v1/auth/authenticate")
	 Authentication authenticate();
}
