package com.rentspace.userservice.service;

import java.util.Optional;
import java.util.UUID;

import com.rentspace.userservice.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rentspace.userservice.entity.User;
import com.rentspace.userservice.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User createUser(User user) {
		return userRepository.save(user);
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Optional<User> getUserById(UUID id) {
		return userRepository.findById(id);
	}

	public User updateUser(UUID id, User userDetails) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		user.setName(userDetails.getName());
		user.setEmail(userDetails.getEmail());
		user.setPassword(userDetails.getPassword());
		user.setRole(userDetails.getRole());
		user.setCreatedAt(userDetails.getCreatedAt());
		return user;
	}

	public void deleteUser(UUID id) {
		userRepository.deleteById(id);
	}
}
