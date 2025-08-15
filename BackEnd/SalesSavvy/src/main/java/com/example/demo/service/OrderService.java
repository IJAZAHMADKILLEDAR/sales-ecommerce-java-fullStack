package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Users;
import com.example.demo.entity.productimage;
import com.example.demo.entity.products;
import com.example.demo.repositary.OrderItemRepositary;
import com.example.demo.repositary.ProductsRespositary;
import com.example.demo.repositary.productImageRepositary;
@Service
public class OrderService {
	
	private OrderItemRepositary orderItemRepositary;
	private ProductsRespositary productsRepositary;
	private productImageRepositary productimageRepositary;
	public OrderService( OrderItemRepositary orderItemRepositary,
			ProductsRespositary productsRepositary, productImageRepositary productimageRepositary) {
		// TODO Auto-generated constructor stub
		this.orderItemRepositary = orderItemRepositary;
		this.productsRepositary = productsRepositary;
		this.productimageRepositary = productimageRepositary;
		
	}
	
	
	
	public Map<String,Object> getOrdersForUser(Users user) {
		List<OrderItem> orderItems = orderItemRepositary.findSuccessfulOrderItemsByUserId(user.getUserId());
		
		Map<String,Object> response = new HashMap<>();
		response.put("username", user.getUsername());
		response.put("role", user.getRole());
		
		List<Map<String,Object>> productt = new ArrayList<>();
		for(OrderItem item :orderItems) {
		products product = productsRepositary.findById(item.getProductId()).orElseThrow(null);
		
		if(product == null) {
			continue;
		}
		
		List<productimage> image = productimageRepositary.findByproduct_productId(product.getProductId());
		String imageurl = image.isEmpty()? null : image.get(0).getImageUrl();
		
		Map<String, Object> productDetails = new HashMap<>();
		productDetails.put("order_id", item.getOrders().getOrderId());
		productDetails.put("quantity", item.getQuantity());
		productDetails.put("total_price", item.getTotalPrice());
		productDetails.put("image_url",imageurl);
		productDetails.put("product_id",product.getProductId());
		productDetails.put("name", product.getName());
		productDetails.put("description", product.getDescription());
		productDetails.put("price_per_unit", item.getPricePerUnit());
		productt.add(productDetails);
		
		}
		response.put("products", productt);
		return response;
		
		
	}
}
