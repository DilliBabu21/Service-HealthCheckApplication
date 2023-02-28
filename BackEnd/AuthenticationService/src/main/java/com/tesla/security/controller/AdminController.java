package com.tesla.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tesla.security.model.AuthenticationRequest;
import com.tesla.security.model.AuthenticationResponse;
import com.tesla.security.model.RegisterRequest;
import com.tesla.security.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminservice;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(adminservice.register(request));

	}

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(adminservice.authenticate(request));
	}
	
	 @PostMapping("/logout")
	    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
	        String jwtToken = authorizationHeader.substring(7);
	        adminservice.logout(jwtToken);
	        return ResponseEntity.ok("Logged out successfully");
	    }

//	@PostMapping("/login")
//	public ResponseEntity<String> login(@RequestBody AuthenticationRequest loginRequest) {
//		adminservice.login(loginRequest.getUsername(), loginRequest.getPassword());
//		return new ResponseEntity<String>("Logged In",HttpStatus.OK);
//	}
	
	

}
