package com.zestbear.bitcoin.mybitcoin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class MybitcoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybitcoinApplication.class, args);
	}

}
