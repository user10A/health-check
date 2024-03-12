package healthcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HealthCheckJava11Application {

	public static void main(String[] args) {
		SpringApplication.run(HealthCheckJava11Application.class, args);
		System.setProperty("java.awt.headless", "true");
	}

}
