package com.terraform.dao;

import java.util.Optional;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.terraform.model.AuthModel;



@EnableScan
@Repository
public interface AuthenticationDAO extends CrudRepository<AuthModel, String> {

//	Optional<UserModel> findByUserName(String username);
	
	Optional<AuthModel> findByEmail(String email);
	
//	boolean findByUserNameAvailability(String userName);
}

