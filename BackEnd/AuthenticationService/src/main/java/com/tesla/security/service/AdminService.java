package com.tesla.security.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tesla.security.model.Admin;
import com.tesla.security.model.AuthenticationRequest;
import com.tesla.security.model.AuthenticationResponse;
import com.tesla.security.model.RegisterRequest;
import com.tesla.security.repo.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest request) {
		// TODO Auto-generated method stub
		var admin = Admin.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.build();
		adminRepository.save(admin);
		var jwtToken = jwtService.generateToken(admin);
		return AuthenticationResponse.builder()
				.message("Registered")
				.token(jwtToken)
				.build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		// TODO Auto-generated method stub
		var admin = adminRepository.findByUsername(request.getUsername()).orElseThrow();
		var jwtToken = jwtService.generateToken(admin);
		return AuthenticationResponse.builder()
				.message("LoggedIn")
				.token(jwtToken)
				.build();
	}
	public void logout(String jwtToken) {
		jwtService.invalidateToken(jwtToken);
	}
	
//	 public ResponseEntity<String> login(String username, String password) {
//		    Admin admin = adminRepository.findByUsername(username).orElseThrow();
//
//		    if (admin == null) {
//		      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//		    }
//
////		    String token = JWT.create()
////		        .withSubject(user.getUsername())
////		        .withExpiresAt(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
////		        .sign(Algorithm.HMAC512(JWT_SECRET.getBytes()));
//		    
//		    var token = jwtService.generateToken(admin);
//				AuthenticationResponse.builder()
//					.message("LoggedIn")
//					.token(token)
//					.build();
//
//		    HttpHeaders headers = new HttpHeaders();
//		    headers.set("Authorization", "Bearer " + token);
//		    return ResponseEntity.ok().headers(headers).build();
//		  }

}
