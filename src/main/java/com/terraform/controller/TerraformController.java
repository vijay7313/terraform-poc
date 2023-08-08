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

import com.terraform.dto.ServiceRequestDTO;
import com.terraform.dto.TerraformDTO;
import com.terraform.service.TerraformService;
import com.terraform.utils.ResponseHandler;

@RestController
@RequestMapping("/terraform")
public class TerraformController {

	@Autowired
	TerraformService terraformService;

	Map<String, Object> responseData = new HashMap<String, Object>();

	@PostMapping("/createService")
	@PreAuthorize("hasAuthority('RH')")
	public ResponseEntity<?> createService(@RequestBody ServiceRequestDTO serviceRequestDTO) {

		String serviceType = terraformService.serviceActionByRH(serviceRequestDTO);
		
		if (serviceRequestDTO.getRequestStatus().equals("Approved")) 
		{
			responseData.put("message", serviceType + " created successfully!");
			responseData.put("statusCode", HttpStatus.OK.value());
			
		} else {
			
			responseData.put("message", "Service Request "+serviceRequestDTO.getRequestStatus()+"!");
			responseData.put("statusCode", HttpStatus.OK.value());
		}

		return ResponseHandler.generateResponse("", responseData, null);
	}

}
