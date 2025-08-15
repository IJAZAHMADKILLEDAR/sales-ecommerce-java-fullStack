package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
@Entity
@Table(name="users")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	 @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<JwtToken> tokens;
	@Column
	private String username;
	
	@Column
	private String email;
	
	@Column
	private String password;
	
	
	@Enumerated(EnumType.STRING)
	@Column
	private Role role;
	
	@Column
	private LocalDateTime create_at = LocalDateTime.now();
	
	@Column
	private LocalDateTime update_at = LocalDateTime.now();

	public Users(Integer userId, String username, String email, String password, Role role, LocalDateTime create_at,
			LocalDateTime update_at) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.create_at = create_at;
		this.update_at = update_at;
	}

	public Users(String username, String email, String password, Role role, LocalDateTime create_at,
			LocalDateTime update_at) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
		this.create_at = create_at;
		this.update_at = update_at;
	}
public Users() {
	// TODO Auto-generated constructor stub
}
	

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public LocalDateTime getCreate_at() {
		return create_at;
	}

	public void setCreate_at(LocalDateTime create_at) {
		this.create_at = create_at;
	}

	public LocalDateTime getUpdate_at() {
		return update_at;
	}

	public void setUpdate_at(LocalDateTime update_at) {
		this.update_at = update_at;
	}

	}
