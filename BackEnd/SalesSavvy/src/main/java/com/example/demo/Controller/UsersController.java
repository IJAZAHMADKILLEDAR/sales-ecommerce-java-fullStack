package com.example.demo.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Users;
import com.example.demo.service.UserService;
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UsersController {
	
	private final UserService userService;

	public UsersController(UserService userService) {
		// TODO Auto-generated constructor stub
		this.userService = userService;
	}
	@PostMapping("/register")
	public ResponseEntity<?> registerservice(@RequestBody Users user){
		try {
			Users registereds = userService.RegisterUser(user);
			return ResponseEntity.ok(Map.of("user","registerd succesfully","users",registereds));
			
		} catch(RuntimeException e){
		return ResponseEntity.badRequest().body(Map.of("Error",e.getMessage()));	
		}
	}

}
