package com.terraform.dto;

public class AuthDTO {

	private String employeeId;
	private String userName;
	private String email;
	private String password;
	private String role;
	private String reportingHead;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getReportingHead() {
		return reportingHead;
	}

	public void setReportingHead(String reportingHead) {
		this.reportingHead = reportingHead;
	}

}
