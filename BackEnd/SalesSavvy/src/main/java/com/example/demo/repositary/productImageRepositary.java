package com.example.demo.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.productimage;
@Repository
public interface productImageRepositary extends JpaRepository<productimage, Integer> {

	List<productimage> findByproduct_productId(Integer product);
}
