package com.example.demo.repositary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.categories;

@Repository
public interface CategoriesRepositary extends JpaRepository<categories,Integer> {

	Optional<categories> findBycategoryname(String categoryname);
}
