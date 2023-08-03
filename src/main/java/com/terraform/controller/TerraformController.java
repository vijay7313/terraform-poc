package com.terraform.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.terraform.dto.TerraformDTO;
import com.terraform.service.TerraformService;
import com.terraform.utils.ResponseHandler;

@RestController
@RequestMapping("/terraform")
public class TerraformController {
	
	

	@Autowired
	TerraformService terraformService;

	@PostMapping("/createBucket")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<?> createS3Bucket(@RequestBody TerraformDTO terraformDTO) {

		Map<String, Object> apiData = new HashMap<String, Object>();
		Map<String, Object> errorData = new HashMap<>();
		
		String response = terraformService.createS3Bucket(terraformDTO);
		
		apiData.put("message", "S3 bucket created successfully!");
		apiData.put("statusCode", HttpStatus.OK.value());
		return ResponseHandler.generateResponse("", apiData, null);
	}

	@PostMapping("/createEC2Iinstance")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<?> createEC2(@RequestBody TerraformDTO terraformDTO) {

		Map<String, Object> apiData = new HashMap<String, Object>();
		Map<String, Object> errorData = new HashMap<>();
		
		String response = terraformService.createEC2(terraformDTO);

		apiData.put("message", "EC2 instance created successfully!");
		apiData.put("statusCode", HttpStatus.OK.value());
		
		return ResponseHandler.generateResponse("", apiData, null);
	}

}
