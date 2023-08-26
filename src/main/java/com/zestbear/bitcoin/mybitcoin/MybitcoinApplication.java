package com.zestbear.bitcoin.mybitcoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class MybitcoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybitcoinApplication.class, args);
	}

}
