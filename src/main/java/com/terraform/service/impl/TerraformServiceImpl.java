package com.terraform.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.terraform.dto.TerraformDTO;
import com.terraform.service.TerraformService;
import com.terraform.utils.CommonUtilsForTerraform;

@Service
public class TerraformServiceImpl implements TerraformService {

	@Autowired
	CommonUtilsForTerraform commonUtilsForTerraform;

	String terraformBinary = "C:\\Program Files\\Terraform\\terraform";
	String terraformWorkingDir = "V:\\terraform\\commonFile";

//	String terraformBinary = "/usr/bin/terraform";
//	String terraformWorkingDir = "/home/ec2-user/terraform/terraformfiles";

	@Override
	public void createS3Bucket(TerraformDTO terraformDTO) {

		String[] initCommands = { terraformBinary, "init" };
		commonUtilsForTerraform.executeTerraformCommand(initCommands, terraformWorkingDir, terraformDTO);

		String[] planCommands = { terraformBinary, "plan", "-out=tfplan", "-var",
				"bucket_name=" + terraformDTO.getS3BucketName() };
		commonUtilsForTerraform.executeTerraformCommand(planCommands, terraformWorkingDir, terraformDTO);

		String[] applyCommands = { terraformBinary, "apply", "tfplan" };
		commonUtilsForTerraform.executeTerraformCommand(applyCommands, terraformWorkingDir, terraformDTO);

		commonUtilsForTerraform.deleteFilesExceptMainTf(terraformWorkingDir);

	}

	@Override
	public void createEC2(TerraformDTO terraformDTO) {
		
		String[] initCommands = { terraformBinary, "init" };
		commonUtilsForTerraform.executeTerraformCommand(initCommands, terraformWorkingDir, terraformDTO);

		String[] planCommands = { terraformBinary, "plan", "-out=tfplan", "-var",
				"instance_name=" + terraformDTO.getEc2InstanceName() };
		commonUtilsForTerraform.executeTerraformCommand(planCommands, terraformWorkingDir, terraformDTO);

		String[] applyCommands = { terraformBinary, "apply", "tfplan" };
		commonUtilsForTerraform.executeTerraformCommand(applyCommands, terraformWorkingDir, terraformDTO);
		
		commonUtilsForTerraform.deleteFilesExceptMainTf(terraformWorkingDir);

	}

}
