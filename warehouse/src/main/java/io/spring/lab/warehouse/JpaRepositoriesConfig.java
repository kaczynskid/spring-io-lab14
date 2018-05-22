package io.spring.lab.warehouse;

import java.math.BigDecimal;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.spring.lab.warehouse.item.Item;
import io.spring.lab.warehouse.item.ItemRepository;

@Configuration
@EnableJpaRepositories(considerNestedRepositories = true)
public class JpaRepositoriesConfig {

    @Bean
    @ConditionalOnProperty(name = "test.data", havingValue = "true", matchIfMissing = true)
    ApplicationRunner initData(ItemRepository items) {
        return args -> {
            items.save(new Item(null, "A", 100, BigDecimal.valueOf(40.0)));
            items.save(new Item(null, "B", 100, BigDecimal.valueOf(10.0)));
            items.save(new Item(null, "C", 100, BigDecimal.valueOf(30.0)));
            items.save(new Item(null, "D", 100, BigDecimal.valueOf(25.0)));
        };
    }
}
