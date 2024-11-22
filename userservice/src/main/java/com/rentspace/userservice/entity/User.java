package com.rentspace.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import static jakarta.persistence.GenerationType.*;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

}
