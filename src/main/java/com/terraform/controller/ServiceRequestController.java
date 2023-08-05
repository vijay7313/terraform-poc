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

	Map<String, Object> apiData = new HashMap<String, Object>();

	@PostMapping("/createRequest")
	@PreAuthorize("hasAuthority('RH') or hasAuthority('EMPLOYEE')")
	public ResponseEntity<?> createRequestForService(@RequestBody ServiceRequestDTO serviceRequestDTO) {

		terraformService.createService(serviceRequestDTO);

		apiData.put("message", "Service request created successfully!");
		apiData.put("statusCode", HttpStatus.OK.value());

		return ResponseHandler.generateResponse("", apiData, null);

	}

	@PostMapping("/getAllRequestedServices")
	@PreAuthorize("hasAuthority('RH') or hashAuthority('EMPLOYEE')")
	public ResponseEntity<?> getRequstedDetails(@RequestBody ServiceRequestDTO serviceRequestDTO) {

		Map<String, Object> getAllRequests = new HashMap<String, Object>();

		List<ServiceRequestDTO> response = terraformService.getAllRequestedServices(serviceRequestDTO);
		if (!response.isEmpty()) {
			apiData.put("message", "requests fetched successfully!");
			apiData.put("statusCode", HttpStatus.OK.value());
			getAllRequests.put("getAllRequests", response);
			return ResponseHandler.generateResponse("", apiData, getAllRequests);
		}
		apiData.put("message", "No service requests found!");
		apiData.put("statusCode", HttpStatus.OK.value());
		return ResponseHandler.generateResponse("", apiData, "");

	}
}
