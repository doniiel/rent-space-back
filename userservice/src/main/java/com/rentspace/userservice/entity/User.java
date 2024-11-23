package com.rentspace.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.*;

import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.rentspace.userservice.util.Role;

import jakarta.persistence.Column;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private UUID id;

	@Column(name = "name")
	private String name;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Column(name = "created_at")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createdAt;

}
