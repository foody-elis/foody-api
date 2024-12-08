package com.example.foody;

import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.service.impl.AuthenticationServiceImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

@SpringBootApplication
public class FoodyApplication {
	@Value("${spring.application.admin.email}")
	private String adminEmail;

	@Value("${sprimg.application.admin.password}")
	private String adminPassword;

	public static void main(String[] args) {
		SpringApplication.run(FoodyApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationServiceImpl authenticationServiceImpl) {
		return args -> {
			final Logger logger = LogManager.getLogger(FoodyApplication.class.getName());

			String path = "first-boot.txt";
			File file = new File(path);

			if (!file.exists()) {
				UserRequestDTO adminDTO = new UserRequestDTO(adminEmail, adminPassword, "Admin", "Admin", LocalDate.of(1999, 1, 1), "0123456789", null , null);

				authenticationServiceImpl.registerAdmin(adminDTO);

				try (FileWriter writer = new FileWriter(path)) {
					writer.write("Admin account" + "\n");
					writer.write("Email: " + adminEmail + "\n");
					writer.write("Password: " + adminPassword + "\n");

					logger.log(Level.INFO, "'first-boot.txt' file created.");

					// I set the file as hidden
					Process p = Runtime.getRuntime().exec(new String[]{"attrib", "+h", path});
					p.waitFor();
				} catch (IOException e) {
					logger.log(Level.ERROR, "Error while trying to write the admin credentials to the file.");
				}
			}
		};
	}
}
