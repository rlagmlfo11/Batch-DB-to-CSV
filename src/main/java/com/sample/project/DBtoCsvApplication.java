package com.sample.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 데이터베이스에있는 정보를 csv로 출력한다
@SpringBootApplication
public class DBtoCsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(DBtoCsvApplication.class, args);
	}

}
