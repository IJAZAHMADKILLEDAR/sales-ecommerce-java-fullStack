package com.example.demo.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Role;
import com.example.demo.entity.Users;
import com.example.demo.repositary.UserRepositary;
import com.example.demo.service.AuthService;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = {"/api/*","/admin/*"})
@Component
public class AuthFilter implements Filter {

	AuthService authService;
	UserRepositary userRepo;
	private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
	private static final String ALLOWED_ORGIN="http://localhost:3000";
	public static final String[] UNAUTHENTICATED_PATHS = {
			"/api/users/register",
			"/api/auth/login",
			
			
	};

	
	public AuthFilter(AuthService authService,
	UserRepositary userRepo) {
		System.out.println("filter started");
		// TODO Auto-generated constructor stub
		this.authService = authService;
		this.userRepo = userRepo;
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		
		try {
			
			executeFilterLogin(request,response,chain);
			
		}catch (Exception e) {
			logger.error("Unexpected Error in Authtication filter",e);
			// TODO: handle exception
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			sendErrorResponse(httpResponse,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal server Error");
		}
	}
	
	public void executeFilterLogin(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest= (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String requestURI= httpRequest.getRequestURI();
		setCORSHeader(httpResponse);
		logger.info("Request URI : {} ",requestURI );
		if(Arrays.asList(UNAUTHENTICATED_PATHS).contains(requestURI)) {
			chain.doFilter(request, response);
			return;
		}
		
		if(httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
			httpResponse.setHeader("Access-Control-Allow-Origin","http://localhost:3000");
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			return;
		}
		String token = getAuthTokenCookie(httpRequest);
		System.out.println(token);
		if(token == null || !authService.validateToken(token)) {
			sendErrorResponse(httpResponse,HttpServletResponse.SC_UNAUTHORIZED,"Invalis Token" );
			return;
		}
		String username = authService.extractUsername(token);
		Optional<Users> userOptional = userRepo.findByusername(username);
		if(userOptional.isEmpty()) {
			sendErrorResponse(httpResponse,HttpServletResponse.SC_UNAUTHORIZED,"User not Found" );
			return;
		}
		Users authenticatedUser = userOptional.get();
		Role role =authenticatedUser.getRole();
		logger.info("Authenticated User: {}, Role:{}",authenticatedUser.getUsername(),role);
		if(requestURI.startsWith("/api/") && (role!=Role.CUSTOMER && role!= Role.ADMIN)) {
			sendErrorResponse(httpResponse,HttpServletResponse.SC_UNAUTHORIZED,"Not allowed" );
			return;
		}
		if(requestURI.startsWith("/admin/") && role != Role.ADMIN ) {
			sendErrorResponse(httpResponse,HttpServletResponse.SC_UNAUTHORIZED,"NotAllowed" );
			return;
		}
		httpRequest.setAttribute("authenticatedUser", authenticatedUser);
		chain.doFilter(request, response);
	}
	
	public String getAuthTokenCookie(HttpServletRequest request) {
		
		Cookie cookies[] = request.getCookies();
		   if (cookies != null) {
		        for (Cookie cookie : cookies) {
		            if ("authtoken".equals(cookie.getName())) {
		                return cookie.getValue();
		            }
		        }
		    }
		
		return null;
	}
	private void sendErrorResponse(HttpServletResponse response, int statuscode,String message) throws IOException {
		// TODO Auto-generated method stub
		response.setStatus(statuscode);
		response.getWriter().write(message);
		
	}
	void setCORSHeader(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORGIN);
	    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
	    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
	    response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setStatus(HttpServletResponse.SC_OK);;
	}

}
