package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderStatus;
import com.example.demo.entity.Orders;
import com.example.demo.repositary.CartRepositary;
import com.example.demo.repositary.OrderItemRepositary;
import com.example.demo.repositary.OrderRepositary;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.transaction.Transactional;
@Service
public class PaymentService {
	@Value("${razorpay.key_id}")
	private String razorpayKeyId;
	@Value("${razorpay.key_secret}")
	private String razorpayKeySecret;
	private final OrderRepositary orderRepositary;
	private final OrderItemRepositary orderItemrepositary;
	private final CartRepositary cartRepositary;
	
	public PaymentService(OrderRepositary orderRepositary,OrderItemRepositary orderItemrepositary,CartRepositary cartRepositary) {
		// TODO Auto-generated constructor stub
		this.cartRepositary = cartRepositary;
		this.orderRepositary = orderRepositary;
		this.orderItemrepositary = orderItemrepositary;
	}
	@Transactional
	public String createOrder(int userId,BigDecimal totalAmount, List<OrderItem> cartItems) throws RazorpayException {
		RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId , razorpayKeySecret);
		System.out.println(razorpayKeyId  + " " +  razorpayKeySecret);
		var OrderRequest = new JSONObject();
		OrderRequest.put("amount",totalAmount.multiply(BigDecimal.valueOf(100)).intValue());
		OrderRequest.put("currency", "INR");
		OrderRequest.put("receipt", "txn_"+ System.currentTimeMillis());
		System.out.println("OrderRequest = " + OrderRequest.toString(2));

		System.out.println("Service create - 1");
		System.out.println("Razorpay KeyId: " + razorpayKeyId);
		System.out.println("Razorpay KeySecret: " + razorpayKeySecret);

		com.razorpay.Order razorpayOrder = razorpayClient.orders.create(OrderRequest);
		System.out.println("Service create - 2");
		Orders order = new Orders();
		order.setOrderId(razorpayOrder.get("id"));
		order.setUserId(userId);
		order.setStatus(OrderStatus.PENDING);
		order.setTotalAmount(totalAmount);
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());
		System.out.println("Service create - 3");
		orderRepositary.save(order);
		return razorpayOrder.get("id");
			
	}
	@Transactional
	public boolean verifyPayment(String razorpayOrderId,String razorpayPaymentId, String razorpaySignature, int userId ) {
		try {
			
			JSONObject attributes = new JSONObject();
			attributes.put("razorpay_order_id", razorpayOrderId);
			attributes.put("razorpay_payment_id",razorpayPaymentId);
			attributes.put("razorpay_signature", razorpaySignature);
			
			boolean isSignatureValid = com.razorpay.Utils.verifyPaymentSignature(attributes, razorpayKeySecret);
			if(isSignatureValid) {
				Orders order = orderRepositary.findById(razorpayOrderId).orElseThrow(()-> new IllegalArgumentException("Unable to find"));
				order.setStatus(OrderStatus.SUCCESS);
				order.setUpdatedAt(LocalDateTime.now());
				orderRepositary.save(order);
				
				List<CartItem> cartitems = cartRepositary.findCartItemswithProductDetails(userId);
				
				for(CartItem cartitem : cartitems) {
					OrderItem orderitem = new OrderItem();
					orderitem.setOrders(order);
					orderitem.setProductId(cartitem.getProducts().getProductId());
					orderitem.setQuantity(cartitem.getQuantity());
					orderitem.setPricePerUnit(cartitem.getProducts().getPrice());
					orderitem.setTotalPrice(cartitem.getProducts().getPrice().multiply(BigDecimal.valueOf(cartitem.getQuantity())));
					orderItemrepositary.save(orderitem);
				}
				cartRepositary.deleteAllCartItemByUser(userId);
				return true;
			} else {
				return false;
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	@Transactional
	public void saveOrderItem(String orderId, List<OrderItem> items) {
		Orders order = orderRepositary.findById(orderId).orElseThrow(()-> new RuntimeException("Order not found"));
		for(OrderItem item :items) {
			orderItemrepositary.save(item);
		}
	}
	
}
