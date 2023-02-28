package com.tesla.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "services")
public class Services {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private String service_name;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String httpMethod;

	private String payload;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ServiceStatus status = ServiceStatus.UNKNOWN;

	@Column(nullable = false)
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

	@Column(nullable = false)
	private Timestamp checkedAt = new Timestamp(System.currentTimeMillis());



}
