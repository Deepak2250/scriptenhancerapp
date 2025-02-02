package com.scriptenhancer.controllers;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.scriptenhancer.service.UserService;

import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController  {

	@Autowired
	private UserService userService;
	
	@PostMapping("/uploadprofile")
	public ResponseEntity<String> uploadProfile(@RequestBody MultipartFile multipartFile) {
		log.info("The content type is : " + multipartFile.getContentType().contains("image/jpg"));
	    log.info("Original filename: " + multipartFile.getOriginalFilename());
	    log.info("File size: " + multipartFile.getSize());
	    
	    try {
			InputStream inputStream = multipartFile.getInputStream();
			if (multipartFile.isEmpty() || isImage(inputStream , multipartFile.getOriginalFilename())) {
				return ResponseEntity.badRequest().body("You can only upload image in the format of png , jpg , jpeg");
			} 
		} catch (java.io.IOException e) {
			return ResponseEntity.internalServerError().body("Failed to load the file");
		}
		
		userService.uploadImage(multipartFile);
		return ResponseEntity.ok("Image Uploaded Successfully");
	}
	
	@GetMapping("/getprofile")
	public ResponseEntity<byte[]> getMethodName() {
		byte[] image = userService.getProfile();
		   return ResponseEntity.ok()
	                .contentType(MediaType.IMAGE_PNG)
	                .contentType(MediaType.IMAGE_JPEG) // Use IMAGE_PNG if storing PNG
	                .body(image);
	}
	
	
	   private boolean isImage(InputStream fileContent, String fileName) throws IOException {
	        // Check file extension (optional, but not reliable alone)
	        if (!fileName.toLowerCase().endsWith(".png") || !fileName.toLowerCase().endsWith(".jpg") || !fileName.toLowerCase().endsWith(".jpeg")) {
	            return false;
	        }
			return true;
	   }
	   
	   @DeleteMapping("/deleteimage")
	   public ResponseEntity<String> deleteImage(){
		 boolean deletedImage = userService.deletImage();
		 return ResponseEntity.ok().body("The Image has been deleted : " +deletedImage);
	   }
	
}