package com.terraform.dto;

public class TerraformDTO {

	private String s3BucketName;

	private String ec2InstanceName;

	public String getS3BucketName() {
		return s3BucketName;
	}

	public void setS3BucketName(String s3BucketName) {
		this.s3BucketName = s3BucketName;
	}

	public String getEc2InstanceName() {
		return ec2InstanceName;
	}

	public void setEc2InstanceName(String ec2InstanceName) {
		this.ec2InstanceName = ec2InstanceName;
	}

}
