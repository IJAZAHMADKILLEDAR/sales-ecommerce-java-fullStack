package com.example.demo.Controller;

import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.example.demo.entity.Users;
import com.example.demo.repositary.UserRepositary;
import com.example.demo.service.CartService;

import jakarta.servlet.http.HttpServletRequest;

@ComponentScan
@RestController
@CrossOrigin(origins="http://localhost:3000",allowCredentials = "true")
@RequestMapping("/api/cart")
public class CartControler {

	CartService  cartservice;
	UserRepositary userRepositary;
	public CartControler(CartService  cartservice,
	UserRepositary userRepositary) {
		// TODO Auto-generated constructor stub
		this.cartservice = cartservice;
		this.userRepositary =userRepositary;
	}
	
	@GetMapping("/items/count")
	public ResponseEntity<Integer> getCartCount(@RequestParam String username) throws IllegalAccessException {
		Users user = userRepositary.findByusername(username).orElseThrow(()-> new IllegalAccessException("invalid username found" + username));
		int count = cartservice.productCartCount(user.getUserId());
		return ResponseEntity.ok(count);
	}
	@PostMapping("/add")
	public ResponseEntity<Void> addToCart(@RequestBody Map<String,Object> request ) {
		String username = (String)request.get("username");
		int productId =(int) request.get("productId");
		int quantity = request.containsKey("quantity")? (int) request.get("quantity"): 1;
		
		Users user = userRepositary.findByusername(username).orElseThrow(()->new IllegalArgumentException("Username is not found please try again" + username));
		cartservice.addToCart(username, productId, quantity);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	@GetMapping("/items")
	public ResponseEntity<Map<String,Object>> getCartItems(HttpServletRequest request) {
		Users user = (Users)request.getAttribute("authenticatedUser");
		Map<String,Object> cartItems = cartservice.getCartItems(user.getUserId());
		return ResponseEntity.ok(cartItems);
	}
	@PutMapping("/update")
	public ResponseEntity<Void> updateCartItemQuantity(@RequestBody Map<String,Object> request) {
		System.out.println("==== /update endpoint called ====");
		String username = (String)request.get("username");
		int productId = (int)request.get("productId");
		int quantity = (int)request.get("quantity");
		System.out.println(username + " " + productId + " " + quantity); 
		Users user = userRepositary.findByusername(username).orElseThrow(()-> new IllegalArgumentException("Unable to find username"));
		cartservice.updateCartItemQuantity(user.getUserId(), productId, quantity);
		System.out.println("all areOKay?");
	return ResponseEntity.status(HttpStatus.OK).build();
	}
	@DeleteMapping("/delete")
	public ResponseEntity<Void> deleteCartItems(@RequestBody Map<String,Object> request) {
		String username = (String)request.get("username");
		int productId =(int) request.get("productId");
		
		Users user = userRepositary.findByusername(username).orElseThrow(()-> new IllegalArgumentException("User not Found"));
		cartservice.deleteCartItem(user.getUserId(), productId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		
	}
	
	
	
}
