package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.categories;
import com.example.demo.entity.productimage;
import com.example.demo.entity.products;
import com.example.demo.repositary.CategoriesRepositary;
import com.example.demo.repositary.ProductsRespositary;
import com.example.demo.repositary.productImageRepositary;
@Service
public class ProductService {

	
	productImageRepositary productImageRepositary;
	ProductsRespositary productsRepositary;
	CategoriesRepositary categoriesRepositary;
	
	public ProductService(productImageRepositary productImageRepositary,
	ProductsRespositary productsRepositary,
	CategoriesRepositary categoriesRepositary) {
		// TODO Auto-generated constructor stub
		this.productImageRepositary =productImageRepositary;
		this.productsRepositary= productsRepositary;
		this.categoriesRepositary = categoriesRepositary;
	}
	public List<products> getProductsByCategories(String categoryName) {
		if(categoryName != null && !categoryName.isEmpty()) {
			Optional<categories> categoryOpt = categoriesRepositary.findBycategoryname(categoryName);
			if(categoryOpt.isPresent()) {
				categories Categories = categoryOpt.get();
				return productsRepositary.findByCategories_categoryId(Categories.getCategoryId());
			} else {
				throw new RuntimeException("Categories not Found");
			}
		} else {
			return productsRepositary.findAll();
		}
	}
	public List<String> getProductImages(Integer productId) {
		List<productimage> productImages = productImageRepositary.findByproduct_productId(productId);
		
		List<String> imageUrl = new ArrayList();
		for(productimage image : productImages) {
			imageUrl.add(image.getImageUrl());
		}
		return imageUrl;
	}
	
}
