package com.example.demo.Controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Users;
import com.example.demo.repositary.UserRepositary;
import com.example.demo.service.PaymentService;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
@RestController
@CrossOrigin(origins="https://localhost:3000",allowCredentials = "true")
@RequestMapping("/api/payment")
public class PaymentController {
	private PaymentService paymentservice;
	private UserRepositary userRepositary;

	public PaymentController(PaymentService paymentservice,
	 UserRepositary userRepositary) {
		// TODO Auto-generated constructor stub
		this.paymentservice = paymentservice;
		this.userRepositary = userRepositary;
	}

@PostMapping("/create")
	public ResponseEntity<String> createPaymentOrer(@RequestBody Map<String, Object> requestBody,HttpServletRequest request) {
		try {
			Users user = (Users)request.getAttribute("authenticatedUser");
			System.out.println("control create - 1");
			if(user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authinticated");
			}
			BigDecimal totalAmount = new BigDecimal(requestBody.get("totalAmount").toString());
			List<Map<String,Object>> cartItemsRaw = (List<Map<String,Object>>) requestBody.get("cartItems");
			System.out.println("control create - 2");
		List<OrderItem> cartItems =cartItemsRaw.stream().map(item -> {
			OrderItem  orderitem = new OrderItem();
			orderitem.setProductId((Integer) item.get("productId"));
			orderitem.setQuantity((Integer) item.get("quantity"));
			BigDecimal pricePerUnit = new BigDecimal(item.get("price").toString());
			orderitem.setPricePerUnit(pricePerUnit);
			orderitem.setTotalPrice(pricePerUnit.multiply(BigDecimal.valueOf((Integer)item.get("quantity"))));
			return orderitem;
			
		}).collect(Collectors.toList());
		System.out.println("control create - 3");
		String razorpayOrderId = paymentservice.createOrder(user.getUserId(),totalAmount, cartItems);
		return ResponseEntity.ok(razorpayOrderId);
		
		}catch(RazorpayException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating Razorpay order:" + e.getMessage());
			
		}catch (Exception e) {
			// TODO: handle exception
		
		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request data:" + e.getMessage() ); 
		
		}
	}
	@PostMapping("/verify")
	public ResponseEntity<String> verifyPayment(@RequestBody Map<String,Object> requestBody, HttpServletRequest request) {
		try {
			Users user =(Users) request.getAttribute("authenticatedUser");
			if(user == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not authinticated");
			}
			int userId = user.getUserId();
			String razorPayOrderId = (String) requestBody.get("razorpayOrderId");
			String razorpayPaymentId = (String) requestBody.get("razorpayPaymentId");
			String razorpaySignature = (String) requestBody.get("razorpaySignature");
			boolean isVerified = paymentservice.verifyPayment(razorPayOrderId, razorpayPaymentId, razorpaySignature, userId);
			
			if(isVerified) {
				return ResponseEntity.ok("Payment verifies succesfully");
				
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment verifcation failed");	
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error verfiying payment: " + e.getMessage());
		}
	}
	

}
