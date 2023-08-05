package com.terraform.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.springframework.stereotype.Service;
import com.terraform.dto.ServiceRequestDTO;

@Service
public class CommonUtilsForTerraform {

	private static final int MIN_ID = 10000000; // Minimum value of the 8-digit ID (10^7)
	private static final int MAX_ID = 99999999; // Maximum value of the 8-digit ID (10^8 - 1)


	public void executeTerraformCommand(String[] commands, String workingDir, ServiceRequestDTO serviceRequestDTO) {

		try {
			ProcessBuilder processBuilder = new ProcessBuilder(commands);

			processBuilder.redirectErrorStream(true);

			processBuilder.directory(new File(workingDir));

			if (serviceRequestDTO.getRequestFor().equals("S3")) {
				processBuilder.environment().put("TF_VAR_create_s3_bucket", "true");
				processBuilder.environment().put("TF_VAR_bucket_name", serviceRequestDTO.getServiceName());
			}

			if (serviceRequestDTO.getRequestFor().equals("EC2")) {
				processBuilder.environment().put("TF_VAR_create_instance", "true");
				processBuilder.environment().put("TF_VAR_instance_name", serviceRequestDTO.getServiceName());
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

	public String CurrentDateAndTime() {

		LocalDateTime currentDateTime = LocalDateTime.now();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

		String formattedDateTime = currentDateTime.format(formatter);

		return formattedDateTime;
	}

	public String generateRequestId() {

		Random random = new Random();
		int id = random.nextInt(MAX_ID - MIN_ID + 1) + MIN_ID;
		return String.format("%08d", id);

	}
}
