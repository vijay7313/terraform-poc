package com.terraform.service;

import java.util.List;

import com.terraform.dto.ServiceRequestDTO;
import com.terraform.dto.TerraformDTO;

public interface TerraformService {

	String serviceActionByRH(ServiceRequestDTO serviceRequestDTO);

	void createService(ServiceRequestDTO serviceRequestDTO);

	List<ServiceRequestDTO> getAllRequestedServices(ServiceRequestDTO serviceRequestDTO);

}
