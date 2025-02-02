package com.scriptenhancer.service;

import java.awt.Image;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.scriptenhancer.config.EncoderConfig;
import com.scriptenhancer.customexceptions.AlreadyUsedEmail;
import com.scriptenhancer.customexceptions.ImageNotDeleted;
import com.scriptenhancer.customexceptions.ImageNotFound;
import com.scriptenhancer.customexceptions.ImageNotSaved;
import com.scriptenhancer.customexceptions.UsersNotFound;
import com.scriptenhancer.dto.UserDTO;
import com.scriptenhancer.entities.Role;
import com.scriptenhancer.entities.User;
import com.scriptenhancer.model.CustomUserDetails;
import com.scriptenhancer.repository.RoleRepository;
import com.scriptenhancer.repository.UserRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EncoderConfig passwordEncoder;

    @Transactional
    public User addDaoUser(User user) {
        Optional<User> userInstanceOptional = userRepository.findByEmail(user.getEmail());
        userInstanceOptional.ifPresent(x -> {
            throw new AlreadyUsedEmail("The email is already present try with something else");
        });
        if (user.getRoles() == null) { // This checks first that the role field is null
            Role roleId = roleRepository.findByRole("USER");
            user.setRoles(List.of(roleId));
        }

        user.setEmail(user.getEmail());
        user.setPassword(passwordEncoder.passwordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User addUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        existingUser.ifPresent(u -> {
            throw new AlreadyUsedEmail("The email is already present, try with something else");
        });

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.passwordEncoder().encode(userDTO.getPassword()));

        // Restrict roles: Always assign "USER" role for new signups
        Role defaultRole = roleRepository.findByRole("USER");
        if (defaultRole == null) {
            throw new RuntimeException("Default 'USER' role does not exist");
        }
        user.setRoles(List.of(defaultRole)); // Assign only the "USER" role

        userRepository.save(user);
        return user;
    }

    @Transactional
    public String uploadImage(MultipartFile multipartFile){
    	  CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  	    User currentUser = customUserDetails.getUser();
  	    Optional<User> user =  userRepository.findByEmail(currentUser.getEmail());
  	    
  	    if (user.isPresent()) {
  	    	try {
  				user.get().setProfile(multipartFile.getBytes());
  				return "The Image has been saved : " +multipartFile.getOriginalFilename();
  			} catch (java.io.IOException e) {
  				throw new ImageNotSaved("The Image is not saved bcz of this error : " + e);
  			}
		}
  	    throw new UsersNotFound("No User with this email : " +currentUser.getEmail());
    	 
    }
    
    public byte[] getProfile(){
    	 CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   	    User currentUser = customUserDetails.getUser();
   	  Optional<User> user =  userRepository.findByEmail(currentUser.getEmail());
   	  
   	  if (user.isPresent()) {
		if (user.get().getProfile() == null) {
			throw new ImageNotFound("There is no image in the db ");
		}
		byte[] image = user.get().getProfile();
		return image;
	}
   	  throw new UsersNotFound("There is no user for this image");
    }
    

    public boolean deletImage() {
    	 CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	    User currentUser = customUserDetails.getUser();
    	  
    	 int deletedImage = userRepository.deleteUserProfileImage(currentUser.getEmail());
    	   if (deletedImage > 0) {
			return true;
		}
    	   throw new ImageNotDeleted("No Image has been deleted");
    }
    
}