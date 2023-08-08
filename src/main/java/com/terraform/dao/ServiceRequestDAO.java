package com.terraform.dao;

import java.util.List;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.terraform.model.ServiceRequestModel;

@EnableScan
@Repository
public interface ServiceRequestDAO extends CrudRepository<ServiceRequestModel, String> {

	List<ServiceRequestModel> findByReportingHeadEmployeeId(String employeeId);
	
	List<ServiceRequestModel> findByEmplopyeeId(String employeeId);

	ServiceRequestModel findByRequestId(String requestId);
}
