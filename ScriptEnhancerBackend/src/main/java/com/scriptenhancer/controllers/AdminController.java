package com.scriptenhancer.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scriptenhancer.entities.User;
import com.scriptenhancer.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/getallusers")
    public List<User> getResponse() {
        return adminService.getAllUsers();
    }

    @DeleteMapping("/deleteuser")
    public ResponseEntity<String> deleteUser(@RequestParam String email) {
        User user = adminService.deleteUser(email);
        return ResponseEntity.status(HttpStatus.OK).body("The User is successfully deleted : " + user.getEmail());
    }
}
