package com.terraform.service;

import com.terraform.dto.TerraformDTO;

public interface TerraformService {

	String createS3Bucket(TerraformDTO terraformDTO);

	String createEC2(TerraformDTO terraformDTO);
}
