package io.spring.lab.marketing;

import java.math.BigDecimal;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import io.spring.lab.marketing.special.Special;
import io.spring.lab.marketing.special.SpecialRepository;
import io.spring.lab.marketing.special.select.BestSpecialSelector;
import io.spring.lab.marketing.special.select.SpecialSelector;
import io.spring.lab.math.MathProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
public class MarketingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketingApplication.class, args);
	}

}

@Slf4j
@Configuration
class SpecialSelectorConfiguration {

	@Bean
	SpecialSelector specialSelector(MathProperties math) {
		return new BestSpecialSelector(math);
	}

}

@Component
@AllArgsConstructor
class TestDataInitializer implements ApplicationRunner {

	private final SpecialRepository specials;

	@Override
	public void run(ApplicationArguments args) {
		if (specials.findAll().size() == 0) {
			specials.save(new Special(null, 1, 3, BigDecimal.valueOf(70.0)));
			specials.save(new Special(null, 2, 2, BigDecimal.valueOf(15.0)));
			specials.save(new Special(null, 3, 4, BigDecimal.valueOf(60.0)));
			specials.save(new Special(null, 4, 2, BigDecimal.valueOf(40.0)));
		}
	}
}
