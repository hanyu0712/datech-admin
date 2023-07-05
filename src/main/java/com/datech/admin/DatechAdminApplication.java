package com.datech.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.datech.admin.mapper")
@SpringBootApplication
public class DatechAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatechAdminApplication.class, args);
	}

}
