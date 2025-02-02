package com.scriptenhancer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.scriptenhancer.customexceptions.AdminDeletionIsNotAllowed;
import com.scriptenhancer.customexceptions.UsersNotFound;
import com.scriptenhancer.entities.Role;
import com.scriptenhancer.entities.User;
import com.scriptenhancer.repository.RoleRepository;
import com.scriptenhancer.repository.UserRepository;

@Component
public class AdminService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private RoleRepository roleRepository;
	
	public List<User> getAllUsers() {
		List<User> users = userRepository.findAll();
		if (users.isEmpty()) {
			throw new UsersNotFound("No Users Founded");
		}
		return users;
	}

	@Transactional
	public User deleteUser(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		
		if (user.isPresent()) {
			
			boolean isAdmin = user.get().getRoles().stream().anyMatch(r -> r.getRole().equals("ADMIN"));
			
			if (isAdmin) {
				throw new AdminDeletionIsNotAllowed("You Cannot delete yourself");
			}

			userRepository.delete(user.get());
			return user.get();
		}
		
		throw new UsersNotFound("There is no user with this email : " + email);
	}
}