package com.terraform.service;

import com.terraform.dto.TerraformDTO;

public interface TerraformService {

	void createS3Bucket(TerraformDTO terraformDTO);

	void createEC2(TerraformDTO terraformDTO);
}
