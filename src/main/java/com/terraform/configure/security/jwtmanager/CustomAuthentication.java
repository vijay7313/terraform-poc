package com.terraform.configure.security.jwtmanager;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.terraform.dao.AuthenticationDAO;
import com.terraform.exception.ResourceNotFoundException;
import com.terraform.model.AuthModel;




@Component
public class CustomAuthentication implements UserDetailsService {

	@Autowired
	AuthenticationDAO authenticationDAO;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<AuthModel> user = authenticationDAO.findByEmail(email);
		return user.map(CustomAuthenticationSupport::new)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Name", email));
	}
}
