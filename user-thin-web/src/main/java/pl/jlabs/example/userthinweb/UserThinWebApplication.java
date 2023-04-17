package pl.jlabs.example.userthinweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "pl.jlabs.example.feign.client")
public class UserThinWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserThinWebApplication.class, args);
	}

}
