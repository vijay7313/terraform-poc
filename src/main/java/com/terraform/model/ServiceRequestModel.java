package com.terraform.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import lombok.Data;

@Data
@DynamoDBTable(tableName = "terraform-service-request")
public class ServiceRequestModel {

	@DynamoDBHashKey
	private String requestId;

	@DynamoDBAttribute
	private String emplopyeeId;

	@DynamoDBAttribute
	private String employeeName;

	@DynamoDBAttribute
	private String employeeMail;

	@DynamoDBAttribute
	private String reportingHeadEmployeeId;

	@DynamoDBAttribute
	private String serviceName;

	@DynamoDBAttribute
	private String requestFor;

	@DynamoDBAttribute
	private String requestSubject;

	@DynamoDBAttribute
	private String requestDescription;

	@DynamoDBAttribute
	private String requestStatus;

	@DynamoDBAttribute
	private String createdDate;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getEmplopyeeId() {
		return emplopyeeId;
	}

	public void setEmplopyeeId(String emplopyeeId) {
		this.emplopyeeId = emplopyeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeMail() {
		return employeeMail;
	}

	public void setEmployeeMail(String employeeMail) {
		this.employeeMail = employeeMail;
	}

	public String getReportingHeadEmployeeId() {
		return reportingHeadEmployeeId;
	}

	public void setReportingHeadEmployeeId(String reportingHeadEmployeeId) {
		this.reportingHeadEmployeeId = reportingHeadEmployeeId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getRequestFor() {
		return requestFor;
	}

	public void setRequestFor(String requestFor) {
		this.requestFor = requestFor;
	}

	public String getRequestSubject() {
		return requestSubject;
	}

	public void setRequestSubject(String requestSubject) {
		this.requestSubject = requestSubject;
	}

	public String getRequestDescription() {
		return requestDescription;
	}

	public void setRequestDescription(String requestDescription) {
		this.requestDescription = requestDescription;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

}
