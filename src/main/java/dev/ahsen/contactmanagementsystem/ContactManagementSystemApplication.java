package dev.ahsen.contactmanagementsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ContactManagementSystemApplication {

	private static final Logger log = LoggerFactory.getLogger(ContactManagementSystemApplication.class);

	public static void main(String[] args) {
		try {
			SpringApplication.run(ContactManagementSystemApplication.class, args);
		} catch (Exception ex) {
			log.error("Application failed to start", ex);
			System.exit(1);
		}
	}

}
