package com.example.foody;

import com.example.foody.auth.AuthenticationService;
import com.example.foody.dto.request.UserRequestDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.mapper.AddressMapper;
import com.example.foody.mapper.AddressMapperImpl;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.mapper.RestaurantMapperImpl;
import com.example.foody.model.Address;
import com.example.foody.model.Restaurant;
import com.example.foody.model.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
public class FoodyApplication {
	@Value("${admin.email}")
	private String adminEmail;

	@Value("${admin.password}")
	private String adminPassword;

	public static void main(String[] args) {
		SpringApplication.run(FoodyApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthenticationService authenticationService) {
		return args -> {
			final Logger logger = LogManager.getLogger(FoodyApplication.class.getName());

			String path = "first-boot.txt";
			File file = new File(path);

			if (!file.exists()) {
				UserRequestDTO adminDTO = new UserRequestDTO(adminEmail, adminPassword, "Admin", "Admin", LocalDate.of(1999, 1, 1), "0123456789", "avatar.png", null);

				authenticationService.registerAdmin(adminDTO);

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
