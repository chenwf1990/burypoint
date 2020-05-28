package com.miguan.burypoint;

import com.miguan.burypoint.infrastructure.configure.DynamicDataSourceRegister;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableRabbit
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.miguan.burypoint.domain.repositories")
@Import(DynamicDataSourceRegister.class)
public class BuryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuryApplication.class, args);
	}

}
