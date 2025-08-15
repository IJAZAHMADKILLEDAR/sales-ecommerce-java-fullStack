package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="cart_items")
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private Users users;
	@ManyToOne
	@JoinColumn(name="product_id")
	private products Products;
	@Column
	private int quantity;
	public CartItem() {
		// TODO Auto-generated constructor stub
	}
	public CartItem(Integer id, Users users, products products, int quantity) {
		super();
		this.id = id;
		this.users = users;
		Products = products;
		this.quantity = quantity;
	}
	public CartItem(Users users, products products, int quantity) {
		super();
		this.users = users;
		Products = products;
		this.quantity = quantity;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	public products getProducts() {
		return Products;
	}
	public void setProducts(products products) {
		Products = products;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
	
}
