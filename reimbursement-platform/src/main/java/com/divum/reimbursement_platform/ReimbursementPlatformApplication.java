package com.divum.reimbursement_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing // For auto-populating the createdAt and updatedAt.
@EnableAsync
public class ReimbursementPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReimbursementPlatformApplication.class, args);
	}

}
