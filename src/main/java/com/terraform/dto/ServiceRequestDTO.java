package com.terraform.dto;

public class ServiceRequestDTO {

	private String requestId;

	private String emplopyeeId;

	private String employeeName;

	private String employeeMail;

	private String reportingHeadEmployeeId;

	private String serviceName;

	private String requestFor;

	private String requestSubject;

	private String requestDescription;

	private String requestStatus;

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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getReportingHeadEmployeeId() {
		return reportingHeadEmployeeId;
	}

	public void setReportingHeadEmployeeId(String reportingHeadEmployeeId) {
		this.reportingHeadEmployeeId = reportingHeadEmployeeId;
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

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

}
