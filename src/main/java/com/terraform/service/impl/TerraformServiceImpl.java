package com.terraform.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.terraform.dao.AuthenticationDAO;
import com.terraform.dao.ServiceRequestDAO;
import com.terraform.dto.ServiceRequestDTO;
import com.terraform.model.AuthModel;
import com.terraform.model.ServiceRequestModel;
import com.terraform.service.TerraformService;
import com.terraform.utils.CommonUtilsForTerraform;

@Service
public class TerraformServiceImpl implements TerraformService {

	@Autowired
	CommonUtilsForTerraform commonUtilsForTerraform;

	@Autowired
	ServiceRequestDAO serviceRequestDAO;

	@Autowired
	AuthenticationDAO authenticationDAO;

	@Autowired
	ModelMapper modelMapper;

	String terraformBinary = "C:\\Program Files\\Terraform\\terraform";
	String terraformWorkingDir = "V:\\terraform\\commonFile";

//	String terraformBinary = "/usr/bin/terraform";
//	String terraformWorkingDir = "/home/ec2-user/terraform/terraformfiles";

	@Override
	public void createService(ServiceRequestDTO serviceRequestDTO) {

		Optional<AuthModel> authDTO = authenticationDAO.findByEmail(serviceRequestDTO.getEmployeeMail());

		serviceRequestDTO.setEmplopyeeId(authDTO.get().getEmployeeId());

		serviceRequestDTO.setEmployeeName(authDTO.get().getUserName());

		serviceRequestDTO.setEmployeeMail(authDTO.get().getEmail());

		serviceRequestDTO.setReportingHeadEmployeeId(authDTO.get().getReportingHead());

		serviceRequestDTO.setRequestId(commonUtilsForTerraform.generateRequestId());

		serviceRequestDTO.setCreatedDate(commonUtilsForTerraform.CurrentDateAndTime());

		serviceRequestDTO.setRequestStatus("Pending");

		// DTO to Entity
		ServiceRequestModel request = modelMapper.map(serviceRequestDTO, ServiceRequestModel.class);

		// Saved the Entity to DB
		serviceRequestDAO.save(request);

	}

	@Override
	public List<ServiceRequestDTO> getAllRequestedServices(ServiceRequestDTO serviceRequestDTO) {
		
		List<ServiceRequestModel> requestedServiceListModel = null;

		Optional<AuthModel> getEmployeeId = authenticationDAO.findByEmail(serviceRequestDTO.getEmployeeMail());

		String role = getEmployeeId.get().getRole();

		if (role.equals("RH")) {
			requestedServiceListModel = serviceRequestDAO
					.findByReportingHeadEmployeeId(getEmployeeId.get().getEmployeeId());
		}
		
		if (role.equals("EMPLOYEE")) {
			requestedServiceListModel = serviceRequestDAO.findByEmplopyeeId(getEmployeeId.get().getEmployeeId());
		}

		List<ServiceRequestDTO> requestedServiceList = new ArrayList<ServiceRequestDTO>();

		// Entity to DTO
		for (ServiceRequestModel model : requestedServiceListModel) {
			ServiceRequestDTO dto = modelMapper.map(model, ServiceRequestDTO.class);
			requestedServiceList.add(dto);
		}
		
		// Sorting the list by createdDate
		requestedServiceList.sort(Comparator.comparing(ServiceRequestDTO::getCreatedDate,
				Comparator.reverseOrder()));
		
		return requestedServiceList;

	}

	@Override
	public String serviceActionByRH(ServiceRequestDTO serviceRequestDTO) {

		ServiceRequestModel serviceGetByRequestId = serviceRequestDAO.findByRequestId(serviceRequestDTO.getRequestId());

		// Entity to DTO
		ServiceRequestDTO requestIdDataFromDB = modelMapper.map(serviceGetByRequestId, ServiceRequestDTO.class);

		if (serviceRequestDTO.getRequestStatus().equals("Approved")) {

			String[] initCommands = { terraformBinary, "init" };
			commonUtilsForTerraform.executeTerraformCommand(initCommands, terraformWorkingDir, requestIdDataFromDB);

			String[] planCommands = { terraformBinary, "plan", "-out=tfplan", "-var",
					"instance_name=" + requestIdDataFromDB.getServiceName() };
			commonUtilsForTerraform.executeTerraformCommand(planCommands, terraformWorkingDir, requestIdDataFromDB);

			String[] applyCommands = { terraformBinary, "apply", "tfplan" };
			commonUtilsForTerraform.executeTerraformCommand(applyCommands, terraformWorkingDir, requestIdDataFromDB);

			commonUtilsForTerraform.deleteFilesExceptMainTf(terraformWorkingDir);

			requestIdDataFromDB.setRequestStatus("Approved");

			// DTO to Entity
			ServiceRequestModel request = modelMapper.map(requestIdDataFromDB, ServiceRequestModel.class);

			serviceRequestDAO.save(request);
		}

		if (serviceRequestDTO.getRequestStatus().equals("Rejected")) {

			requestIdDataFromDB.setRequestStatus("Rejected");

			// DTO to Entity
			ServiceRequestModel request = modelMapper.map(requestIdDataFromDB, ServiceRequestModel.class);

			serviceRequestDAO.save(request);
		}
		
		AuthModel getRHEmployeeMail = authenticationDAO.findByEmployeeId(requestIdDataFromDB.getReportingHeadEmployeeId());
		
		commonUtilsForTerraform.sendMailSES(requestIdDataFromDB,getRHEmployeeMail.getEmail());

		return requestIdDataFromDB.getRequestFor();

	}

}
