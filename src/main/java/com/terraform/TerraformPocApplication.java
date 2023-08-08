package com.terraform;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;

@SpringBootApplication
public class TerraformPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(TerraformPocApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public AmazonS3Client awsS3Client() {
		return (AmazonS3Client) AmazonS3Client.builder().build();
	}
}
