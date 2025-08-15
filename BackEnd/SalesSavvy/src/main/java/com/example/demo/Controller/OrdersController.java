package com.example.demo.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Users;
import com.example.demo.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
@RestController
@CrossOrigin(origins="http://localhost:3000",allowCredentials = "true")
@RequestMapping("/api")
public class OrdersController {

	private OrderService orderservice;
	public OrdersController(OrderService orderservice) {
		// TODO Auto-generated constructor stub
		this.orderservice = orderservice;
		
	}
	@GetMapping("/orders")
	ResponseEntity<Map<String,Object>> getOrderForUser(HttpServletRequest request) {
		try {
			Users user = (Users) request.getAttribute("authenticatedUser");
			
			if(user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not found"));
			
			}
			Map<String,Object> response = orderservice.getOrdersForUser(user);
			return ResponseEntity.ok(response);
			
		}catch (IllegalArgumentException e) {
			// TODO: handle exception
		return ResponseEntity.status(400).body(Map.of("error",e.getMessage()));
		
		
		}catch (Exception e) {
			// TODO: handle exception
		
		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error","An unexcptecd error occurred"));
		}
	}
}
