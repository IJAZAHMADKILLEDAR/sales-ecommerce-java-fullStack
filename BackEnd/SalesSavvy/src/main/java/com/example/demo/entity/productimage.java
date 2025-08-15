package com.example.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="productimages")
public class productimage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageId;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="product_id")
	private products product;
	@Column
	private String imageUrl;
	
	
	public productimage() {
		// TODO Auto-generated constructor stub
	}


	public productimage(Integer imageId, products product, String imageUrl) {
		super();
		this.imageId = imageId;
		this.product = product;
		this.imageUrl = imageUrl;
	}


	public productimage(products product, String imageUrl) {
		super();
		this.product = product;
		this.imageUrl = imageUrl;
	}


	public Integer getImageId() {
		return imageId;
	}


	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}


	public products getProduct() {
		return product;
	}


	public void setProduct(products product) {
		this.product = product;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	}
