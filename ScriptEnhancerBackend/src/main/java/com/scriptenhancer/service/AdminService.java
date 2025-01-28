package com.scriptenhancer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.scriptenhancer.customexceptions.UsersNotFound;
import com.scriptenhancer.entities.User;
import com.scriptenhancer.repository.UserRepository;

@Component
public class AdminService {

	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUsers() {
		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			throw new UsersNotFound("No Users Founded");
		}
		return users;
	}

	public User deleteUser(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			userRepository.delete(user.get());
			return user.get();
		}
		throw new UsersNotFound("There is no user with this email : " + email);
	}
}