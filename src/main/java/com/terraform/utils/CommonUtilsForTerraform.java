package com.terraform.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.springframework.stereotype.Service;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.terraform.dto.ServiceRequestDTO;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

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

	public String sendMailSES(ServiceRequestDTO employeeDetails, String fromAddress) {
		
		String toAddress = employeeDetails.getEmployeeMail();

		String subject = "Service request was " + employeeDetails.getRequestStatus();

		String body = 
				"<p>Dear " + employeeDetails.getEmployeeName() + ",</p>" +
				"<p>Your request for creating " + 
				employeeDetails.getRequestFor() + " was " + employeeDetails.getRequestStatus() + ".</p>";

		AmazonSimpleEmailService client = 
				AmazonSimpleEmailServiceClientBuilder
				.standard()
				.withRegion(Regions.AP_SOUTH_1)
				.build();

		 SendEmailRequest request = 
				 new SendEmailRequest()
				 .withDestination(new Destination()
				 .withToAddresses(toAddress))
		         .withMessage(new Message()
		         .withBody(new Body()
		         .withHtml(new Content()
		         .withCharset("UTF-8")
		         .withData(body)))
		         .withSubject(new Content()
		         .withCharset("UTF-8")
		         .withData(subject)))
		         .withSource(fromAddress);
		 
		SendEmailResult response = client.sendEmail(request);
		
		return "Success";
	}
}
