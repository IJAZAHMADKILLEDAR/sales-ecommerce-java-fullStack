package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Users;
import com.example.demo.entity.productimage;
import com.example.demo.entity.products;
import com.example.demo.service.ProductService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProductController {
	ProductService productService;
	public ProductController(	ProductService productService) {
		// TODO Auto-generated constructor stub
		this.productService = productService;
	}
	
	@GetMapping
	ResponseEntity<Map<String,Object>> getProducts(@RequestParam String category,HttpServletRequest request) {
		try {	
		Users authenticatedUser = (Users) request.getAttribute("authenticatedUser");
			if(authenticatedUser == null) {
				return ResponseEntity.status(400).body(Map.of("Error","unatinticated User"));
			}
			List<products> product = productService.getProductsByCategories(category);
			
			Map<String, Object> response = new HashMap<>();
			Map<String,String> userInfo = new HashMap<>();
			userInfo.put("name", authenticatedUser.getUsername());
			userInfo.put("role",authenticatedUser.getRole().name());
			response.put("user", userInfo);
			List<Map<String,Object>> productList = new ArrayList<>();
			for(products Productt: product) {
				Map<String,Object> productDetail = new HashMap<>();
				productDetail.put("product_id", Productt.getProductId());
				productDetail.put("name", Productt.getName());
				productDetail.put("Description", Productt.getDescription());
				productDetail.put("price", Productt.getPrice());
				productDetail.put("stock", Productt.getStock());
				List<String> image = productService.getProductImages(Productt.getProductId());
				productDetail.put("images", image);
				productList.add(productDetail);

			
			}
			response.put("products", productList);
			return ResponseEntity.ok(response);
		
	} catch (RuntimeException e) {
		// TODO: handle exception
		return ResponseEntity.badRequest().body(Map.of("Error",e.getMessage()));
		
	}
	}

}
