package com.example.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Users;
import com.example.demo.repositary.UserRepositary;
@Service
public class UserService {

	private final UserRepositary userRepositary;
	private final BCryptPasswordEncoder passWordEncoder;
	
	public UserService(UserRepositary userRepositary) {
		// TODO Auto-generated constructor stub
		this.userRepositary = userRepositary;
		this.passWordEncoder = new BCryptPasswordEncoder();
	}
	
	public Users RegisterUser(Users user) {
		
		if(userRepositary.findByusername(user.getUsername()).isPresent()) {
			throw new RuntimeException("Username is already present");
		}
		if(userRepositary.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email is already Present");
		}
		user.setPassword(passWordEncoder.encode(user.getPassword()));
		return userRepositary.save(user);
	}
}
