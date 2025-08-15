package com.example.demo.repositary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Users;
@Repository
public interface UserRepositary extends JpaRepository<Users, Integer> {
	
	Optional<Users> findByEmail(String email);
	Optional<Users> findByusername(String username);

}
