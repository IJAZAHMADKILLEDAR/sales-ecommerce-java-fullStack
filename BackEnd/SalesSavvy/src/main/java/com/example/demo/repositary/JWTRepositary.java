package com.example.demo.repositary;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.JwtToken;

import jakarta.transaction.Transactional;
@Repository
public interface JWTRepositary extends JpaRepository<JwtToken, Integer> {
	JwtToken findByUser_UserId(int userId);
	
	public Optional<JwtToken> findByToken(String token);
	@Transactional
	@Modifying
	@Query("DELETE FROM JwtToken t WHERE t.user.userId = :userId")
	void deleteByUserId(@Param("userId") int userId);
}
