package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.Users;

import com.example.demo.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@RestController
@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true")
public class AuthController {

	AuthService authService;
	
	public AuthController(AuthService authService) {
		// TODO Auto-generated constructor stub
		this.authService = authService;
	}
	@PostMapping("/api/auth/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,HttpServletResponse response) {
		try {
			System.out.println("login - 1 ");
		Users user = authService.authinticate(loginRequest.getUsername(), loginRequest.getPassword());
		System.out.println("login - 2 " + user);
		
			String token = authService.generateTokens(user);
			Cookie cookie = new Cookie("authtoken",token);
			cookie.setSecure(false);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			cookie.setMaxAge(3600);
			response.addCookie(cookie);
			System.out.println("login - 3 ");
					Map<String ,String> responseBody = new HashMap<>();
					responseBody.put("message", "Login Successfully");
					responseBody.put("role", user.getRole().name());
					System.out.println("login - 4 ");
					return ResponseEntity.ok(responseBody);
					
		}catch (RuntimeException e) {
			System.out.println("login - no use ");
			// TODO: handle 
return ResponseEntity.status(HttpServletResponse.SC_FORBIDDEN).body(Map.of("error ijaz",e.getMessage()));
}

	}
	@PostMapping("api/auth/logout")
	public ResponseEntity<Map<String,String>> logout (HttpServletRequest request, HttpServletResponse response) {
		try {
		Users user = (Users) request.getAttribute("authenticatedUser");
		authService.logout(user);
		
		Cookie cookie = new Cookie("authToken",null);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		Map<String,String> responseBody = new HashMap<>();
		responseBody.put("message", "Logout succesfully");
		return ResponseEntity.ok(responseBody);
	}catch (RuntimeException e) {
		// TODO: handle exception
	Map<String,String> errorResponse = new HashMap<>();
	errorResponse.put("message", "Logout failed");
	return ResponseEntity.status(500).body(errorResponse );
	
	}
	}
	
}
