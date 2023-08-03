package com.terraform.service.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.terraform.dao.AuthenticationDAO;
import com.terraform.model.AuthModel;
import com.terraform.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	AuthenticationDAO authenticationDAO;


	@Override
	public String getUserNameUsingEmail(String email) {
		Optional<AuthModel> user=authenticationDAO.findByEmail(email);
		return user.get().getUserName();
	}
	
	

}
