package com.terraform.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.stereotype.Service;
import com.terraform.dto.TerraformDTO;

@Service
public class CommonUtilsForTerraform {
	
	
	public void executeTerraformCommand(String[] commands, String workingDir, TerraformDTO terraformDTO) {

		try {

			ProcessBuilder processBuilder = new ProcessBuilder(commands);

			processBuilder.redirectErrorStream(true);

			processBuilder.directory(new File(workingDir));

			if (!(terraformDTO.getS3BucketName() == null)) {
				processBuilder.environment().put("TF_VAR_create_s3_bucket", "true");
				processBuilder.environment().put("TF_VAR_bucket_name", terraformDTO.getS3BucketName());
			}

			if (!(terraformDTO.getEc2InstanceName() == null)) {
				processBuilder.environment().put("TF_VAR_create_instance", "true");
				processBuilder.environment().put("TF_VAR_instance_name", terraformDTO.getEc2InstanceName());
			}

			Process process = processBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}

			int exitCode = process.waitFor();

			if (exitCode != 0) {
				throw new RuntimeException("Terraform command execution failed with exit code: " + exitCode);
			}
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException("Error executing Terraform command: " + e.getMessage(), e);
		}
	}


	public void deleteFilesExceptMainTf(String terraformWorkingDir) {
		try {
			String directoryPath = terraformWorkingDir;

			File directory = new File(directoryPath);

			if (directory.exists() && directory.isDirectory()) {

				File[] files = directory.listFiles();

				if (files != null) {

					for (File file : files) {

						if (file.isFile() && !file.getName().equals("main.tf"))
							file.delete();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error deleting files: " + e.getMessage());
		}
	}
}
