package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name="products")
public class products {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productId;
	@Column
	private String name;
	@Column
	private String description;
	@Column
	private BigDecimal  price;
	@Column
	private Integer stock;
	@ManyToOne()
	@JoinColumn(name="category_id")
	private categories categories;
	@Column
	private LocalDateTime createdAt;
	@Column
	private LocalDateTime updatedAt;
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<productimage> productImages;
	public products() {
		// TODO Auto-generated constructor stub
	}
	public products(Integer productId, String name, String description, BigDecimal price, Integer stock,
			categories categories, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.productId = productId;
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		categories = categories;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public products(String name, String description, BigDecimal price, Integer stock, categories categories,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.name = name;
		this.description = description;
		this.price = price;
		this.stock = stock;
		categories = categories;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public categories getCategories() {
		return categories;
	}
	public void setCategories(categories categories) {
		categories = categories;
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

}
