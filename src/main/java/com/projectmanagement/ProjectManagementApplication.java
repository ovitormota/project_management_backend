package com.projectmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class ProjectManagementApplication {


	public static void main(String[] args) {
		SpringApplication.run(ProjectManagementApplication.class, args);
	}

}
