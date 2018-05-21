package io.spring.lab.marketing;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.spring.lab.marketing.special.Special;
import io.spring.lab.marketing.special.SpecialRepository;

@Configuration
public class TestDataConfiguration {

    @Autowired
    SpecialRepository specials;

    @Bean
    ApplicationRunner specialsTestData() {
        return args -> {
            specials.save(new Special(null, 1, 3, BigDecimal.valueOf(70)));
            specials.save(new Special(null, 2, 2, BigDecimal.valueOf(15)));
            specials.save(new Special(null, 3, 4, BigDecimal.valueOf(60)));
            specials.save(new Special(null, 4, 2, BigDecimal.valueOf(40)));
        };
    }
}
