package com.example.demo.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.JwtToken;
import com.example.demo.entity.Users;
import com.example.demo.repositary.JWTRepositary;
import com.example.demo.repositary.UserRepositary;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {
  private final Key SIGNING_KEY;
	private final UserRepositary userRepositary;
	JWTRepositary jwtRepositary;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public AuthService(UserRepositary userRepositary,
	JWTRepositary jwtRepositary,@Value("${jwt.secret}") String jwtsc) {
		// TODO Auto-generated constructor stub
		this.userRepositary = userRepositary;
		this.jwtRepositary = jwtRepositary;
	this.passwordEncoder = new BCryptPasswordEncoder();
	this.SIGNING_KEY=Keys.hmacShaKeyFor(jwtsc.getBytes(StandardCharsets.UTF_8));
	
	}
	public Users authinticate(String username,String password) {
		System.out.println("Auth Service - 1 ");
		Users user = userRepositary.findByusername(username)
				.orElseThrow(()->  new RuntimeException("Invalid user Name.."));
		System.out.println("Auth Service - 2 ");
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid Password");
		}
		return user;
	}
	public String generateTokens(Users user) {
		System.out.println("Auth Service - 3 ");
		String Token;
		LocalDateTime currentTime = LocalDateTime.now();
		System.out.println("Auth Service - 3.5 " + user.getUserId());
		JwtToken ExisitingTime = jwtRepositary.findByUser_UserId(user.getUserId());
		System.out.println("Auth Service - 4 " + user.getUserId());
		if(ExisitingTime != null && currentTime.isBefore(ExisitingTime.getExpiresAt())) {
			System.out.println("Auth Service - 5 ");
			Token = ExisitingTime.getToken();
		} else {
			System.out.println("Auth Service - 6 ");
			Token = generateNewToken(user);
			if(ExisitingTime!=null) {
				jwtRepositary.delete(ExisitingTime);
			}
			System.out.println("Auth Service - 7 ");
			SaveToken(user, Token);
			System.out.println("Existing token found" + (ExisitingTime != null) );
		}
		
		
		return Token;
	}
	public String generateNewToken(Users user) {
		return Jwts.builder()
				.setSubject(user.getUsername())
				.claim("role",user.getRole().name())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+3600000))
				.signWith(SIGNING_KEY)
				.compact();
		
	}
	
	public void SaveToken(Users user, String token) {
		System.out.println("saving token to db" + token);
		JwtToken ttoken = new JwtToken(user,token,LocalDateTime.now(),LocalDateTime.now().plusHours(1));
		jwtRepositary.save(ttoken);
	}
	public boolean validateToken(String token) {
		
		System.out.println("Validate Token");
		try {
			Jwts.parserBuilder()
			.setSigningKey(SIGNING_KEY)
			.build()
			.parseClaimsJws(token);
Optional<JwtToken> jwttoken = jwtRepositary.findByToken(token);
if(jwttoken.isPresent()) {
	return jwttoken.get().getExpiresAt().isAfter(LocalDateTime.now());
}
return false;
		}
		catch (Exception e) {
		System.out.println("Token validation failed"+ e.getMessage());
		return false;
		}
	}
	public String extractUsername(String token) {
		try {
		return Jwts.parserBuilder().setSigningKey(SIGNING_KEY)
				.build().parseClaimsJws(token)
				.getBody().getSubject();
		} catch(Exception e) {
			System.out.println("failed to extract username" + e.getMessage());
			return null;
		}
	}
	
	public void logout(Users user) {
		int userid = user.getUserId();
		JwtToken token =jwtRepositary.findByUser_UserId(userid);
	if(token != null) {
		jwtRepositary.deleteByUserId(userid);
	}
	}
	
}
