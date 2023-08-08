package com.terraform.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.terraform.configure.security.jwtmanager.JWTTokenUtil;
import org.springframework.http.HttpStatus;
import com.terraform.dto.AuthDTO;
import com.terraform.service.UserService;
import com.terraform.utils.ResponseHandler;

@RestController
@CrossOrigin
public class AuthenticationController {

	@Autowired
	JWTTokenUtil jwtTokenUtil;
	
	@Autowired
	UserService userService;

	@Autowired(required = true)
	AuthenticationManager authenticationManager;

	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthDTO authDTO) {

		Map<String, Object> responseData = new HashMap<String, Object>();
		Map<String, Object> errorData = new HashMap<>();

		try {

			Authentication authenticate = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
			 UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
			 String role = userDetails.getAuthorities().stream()
			          .findFirst()
			          .map(GrantedAuthority::getAuthority)
			          .orElse("defaultRole");
			 String userName=userService.getUserNameUsingEmail(authDTO.getEmail());
			 
			String token = jwtTokenUtil.generateToken(userName,authDTO.getEmail(),role);

			responseData.put("message", "LoggedIn Successfully");
			responseData.put("statusCode", HttpStatus.OK.value());

			return ResponseHandler.generateResponse(token, responseData, null);
		} 
		
		catch (BadCredentialsException badCredentialsException) {
			errorData.put("message", "Incorrect password");
			errorData.put("statusCode", HttpStatus.UNAUTHORIZED.value());

			return ResponseHandler.generateResponse("", errorData,"");
		} 
		
		catch (AuthenticationException authenticationException) {
			errorData.put("message", "Invalid email");
			errorData.put("statusCode", HttpStatus.UNAUTHORIZED.value());

			return ResponseHandler.generateResponse("", errorData,"");
		}

	}
}
