package com.example.demo.service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Users;
import com.example.demo.entity.productimage;
import com.example.demo.entity.products;
import com.example.demo.repositary.CartRepositary;
import com.example.demo.repositary.ProductsRespositary;
import com.example.demo.repositary.UserRepositary;
import com.example.demo.repositary.productImageRepositary;
@Service
public class CartService {

	CartRepositary cartRepositary;
	UserRepositary userRepositary;
	ProductsRespositary productsRepositary;
	productImageRepositary productImagerepositary;
	
	
	public CartService(
			CartRepositary cartRepositary,UserRepositary userRepositary,
			ProductsRespositary productsRepositary,productImageRepositary productImagerepositary) {
		// TODO Auto-generated constructor stub
	this.cartRepositary= cartRepositary;
	this.userRepositary = userRepositary;
	this.productsRepositary =productsRepositary;
	this.productImagerepositary = productImagerepositary;
	}
	
	public int productCartCount(int userId) {
		return cartRepositary.countTotalItems(userId);
	}
	public void addToCart(String username,int productId,int quantity)  {
		Users user = userRepositary.findByusername(username).orElseThrow(()->new IllegalArgumentException("Unable to find the username please check" + username));
	products product = productsRepositary.findById(productId).orElseThrow(()-> new IllegalArgumentException("unable to find the product please do search other" ));
	Optional<CartItem> exisitItem = cartRepositary.findByUserAndProduct(quantity, productId);
	if(exisitItem.isPresent()) {
		CartItem cartItem = exisitItem.get();
		cartItem.setQuantity(cartItem.getQuantity() + quantity);
	cartRepositary.save(cartItem);
	
	} else {
		CartItem newItem = new CartItem(user,product,quantity);
		cartRepositary.save(newItem);
	}
	}
	
	public Map<String,Object> getCartItems(int userId) {
		List<CartItem> cartItems = cartRepositary.findCartItemswithProductDetails(userId);
		Map<String, Object> response = new HashMap<>();
		
		Users user = userRepositary.findById(userId).orElseThrow(()-> new IllegalArgumentException("User is not found" ));
		response.put("username", user.getUsername());
		
		response.put("Role", user.getRole().toString());
		
		List<Map<String,Object>> products = new ArrayList<>();
		
		int overallTotalPrice = 0;
		for(CartItem cartItem : cartItems) {
			Map<String,Object> productDetails = new HashMap<>();
			products product = cartItem.getProducts();
			
			List<productimage> productImages = productImagerepositary.findByproduct_productId(product.getProductId());
			String imageUrl = (productImages != null && !productImages.isEmpty())? productImages.get(0).getImageUrl():"default-image-url";
			productDetails.put("product_id", product.getProductId());
			productDetails.put("imageurl", imageUrl);
			productDetails.put("name", product.getName());
			productDetails.put("description", product.getDescription());
			productDetails.put("price_per_unit", product.getPrice());
			productDetails.put("quantity",cartItem.getQuantity());
			productDetails.put("total_price", cartItem.getQuantity() * product.getPrice().doubleValue());
			products.add(productDetails);
			overallTotalPrice += cartItem.getQuantity() * product.getPrice().doubleValue();
		}
		Map<String,Object> cart = new HashMap<>();
		cart.put("products", products);
		cart.put("overall_total_price", overallTotalPrice);
		response.put("cart", cart);
		return response;
		
	}
	public void deleteCartItem(int userId,int productId) {
		Users user = userRepositary.findById(userId).orElseThrow(()->new IllegalArgumentException("User not found" ));
		
		products product = productsRepositary.findById(productId).orElseThrow(()-> new IllegalArgumentException("unable to find the Product"));
		cartRepositary.deleteCartItem(userId, productId);
		
	}
	
	public void updateCartItemQuantity(int userId,int productId,int quantity) {
		Users user = userRepositary.findById(userId).orElseThrow(()-> new IllegalArgumentException("unable to find username" ));
		
		products product = productsRepositary.findById(productId).orElseThrow(()-> new IllegalArgumentException("unable to find the product"));
		
		Optional<CartItem> existItem = cartRepositary.findByUserAndProduct(userId, productId);
		System.out.println("this is done");
	if(existItem.isPresent()) {
		CartItem  cartitem = existItem.get();
		System.out.println("this is also  done");
		if(quantity == 0) {
			deleteCartItem(userId,productId);
		}else {
			cartitem.setQuantity(quantity);
			cartRepositary.save(cartitem);
		}
	}
	

	
	}
	
	
	
	
	
}
