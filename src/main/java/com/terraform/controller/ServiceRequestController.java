package com.terraform.controller;

import java.util.HashMap;
import java.util.List;
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
import com.terraform.service.TerraformService;
import com.terraform.utils.ResponseHandler;

@RestController
@RequestMapping("/serviceRequest")
public class ServiceRequestController {

	@Autowired
	TerraformService terraformService;

	Map<String, Object> responseData = new HashMap<String, Object>();

	@PostMapping("/createRequest")
	@PreAuthorize("hasAuthority('RH') or hasAuthority('EMPLOYEE')")
	public ResponseEntity<?> createRequestForService(@RequestBody ServiceRequestDTO serviceRequestDTO) {

		terraformService.createService(serviceRequestDTO);

		responseData.put("message", "Service request created successfully!");
		responseData.put("statusCode", HttpStatus.OK.value());

		return ResponseHandler.generateResponse("", responseData, null);

	}

	@PostMapping("/getAllRequestedServices")
	@PreAuthorize("hasAuthority('RH') or hasAuthority('EMPLOYEE')")
	public ResponseEntity<?> getRequstedDetails(@RequestBody ServiceRequestDTO serviceRequestDTO) {

		Map<String, Object> getAllRequests = new HashMap<String, Object>();

		List<ServiceRequestDTO> response = terraformService.getAllRequestedServices(serviceRequestDTO);
		if (!response.isEmpty()) {
			responseData.put("message", "requests fetched successfully!");
			responseData.put("statusCode", HttpStatus.OK.value());
			getAllRequests.put("getAllRequests", response);
			return ResponseHandler.generateResponse("", responseData, getAllRequests);
		}
		responseData.put("message", "No service requests found!");
		responseData.put("statusCode", HttpStatus.OK.value());
		return ResponseHandler.generateResponse("", responseData, "");

	}
}
