package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="orders")
public class Orders {
	@Id
	@Column(name="order_id")
	private String orderId;
	
	@Column(name ="user_id",nullable = false)
	private int userId;
	@Column(name="total_amount", nullable=false)
	private BigDecimal totalAmount;
	@Enumerated(EnumType.STRING)
	@Column(name="status",nullable=false)
	private OrderStatus status;
	@Column(name="created_at", nullable=false)
	private LocalDateTime createdAt;
	@Column(name="updated_at", nullable=false)
	private LocalDateTime updatedAt;
	@OneToMany(mappedBy = "orders",cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	private List<OrderItem> orderItem;
	public Orders() {
		// TODO Auto-generated constructor stub
	}
	
	public Orders(String orderId, int userId, BigDecimal totalAmount, OrderStatus status, LocalDateTime createdAt,
			LocalDateTime updatedAt, List<OrderItem> orderItem) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.totalAmount = totalAmount;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.orderItem = orderItem;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public List<OrderItem> getOrderItem() {
		return orderItem;
	}
	public void setOrderItem(List<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}
	
}
