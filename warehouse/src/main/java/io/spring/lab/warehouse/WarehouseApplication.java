package io.spring.lab.warehouse;

import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class WarehouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseApplication.class, args);
	}
}

@Component
class RandomHealthIndicator implements HealthIndicator {

	private static final Random RANDOM = new Random();

	@Override
	public Health health() {
		return Health.status(RANDOM.nextBoolean() ? Status.UP : Status.DOWN)
				.withDetail("someNumber", RANDOM.nextLong())
				.build();
	}
}
