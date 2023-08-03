package com.terraform.configure.security.jwtmanager;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	JWTTokenUtil jwtTokenUtil;

	@Autowired
	CustomAuthentication customAuthentication;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Map<String, Object> responseData = new HashMap<>();
		String errorMessage = null;
		int statusCode = 200; // Default status code for success

		try {

			String authHeader = request.getHeader("Authorization");
			String token = null, userName = null;

			if (authHeader != null && authHeader.startsWith("Bearer ")) {

				token = authHeader.substring(7);
				userName = jwtTokenUtil.extractEmail(token);

			}

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = customAuthentication.loadUserByUsername(userName);
				boolean tokenValidation = jwtTokenUtil.validateToken(token, userDetails);

				if (tokenValidation) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authToken);

				}
			}
		
		} catch (ExpiredJwtException expiredJwtException) {
			errorMessage = "Token expired";
			statusCode = 401;
		} catch (SignatureException signatureException) {
			errorMessage = "Invalid token signature";
			statusCode = 401;
		} catch (AuthenticationException authenticationException) {
			errorMessage = "Authentication failed";
			statusCode = 401;
		}
		if (errorMessage != null) {
			responseData.put("message", errorMessage);
			responseData.put("statusCode", statusCode);

			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("responseData", responseData);
			responseMap.put("apiStatus", "");

			response.setStatus(statusCode);
			response.setContentType("application/json");
			response.getWriter().write(new ObjectMapper().writeValueAsString(responseMap));
			return;
		}
		filterChain.doFilter(request, response);
	}
}