package com.example.demo.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.products;
@Repository
public interface ProductsRespositary extends JpaRepository<products, Integer> {

	List<products> findByCategories_categoryId(Integer categoryId);
}
