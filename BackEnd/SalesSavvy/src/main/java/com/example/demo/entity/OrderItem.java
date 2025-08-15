package com.example.demo.entity;

import java.math.BigDecimal;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
@Entity
@Table(name="order_items")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="order_id",nullable=false)
	private Orders orders;
	
	@Column(name="product_id" ,nullable=false)
	private int productId;
	@Column
	private int quantity;

	@Column(name="price_per_unit" , nullable=false)
	private BigDecimal pricePerUnit;
	@Column(name="total_price",nullable=false)
	private BigDecimal totalPrice;
	
	public OrderItem() {
		// TODO Auto-generated constructor stub
	}

	public OrderItem(Orders orders, int productId, int quantity, BigDecimal pricePerUnit, BigDecimal totalPrice) {
		super();
		this.orders = orders;
		this.productId = productId;
		this.quantity = quantity;
		this.pricePerUnit = pricePerUnit;
		this.totalPrice = totalPrice;
	}

	public OrderItem(int id, Orders orders, int productId, int quantity, BigDecimal pricePerUnit,
			BigDecimal totalPrice) {
		super();
		this.id = id;
		this.orders = orders;
		this.productId = productId;
		this.quantity = quantity;
		this.pricePerUnit = pricePerUnit;
		this.totalPrice = totalPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(BigDecimal pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	

	}
