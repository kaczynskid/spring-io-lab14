package io.spring.lab.store;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import io.spring.lab.store.item.ItemRepresentation;
import io.spring.lab.store.item.ItemStockUpdate;
import io.spring.lab.store.item.ItemsClient;
import io.spring.lab.store.special.SpecialCalculation;
import io.spring.lab.store.special.SpecialCalculationRequest;
import io.spring.lab.store.special.SpecialClient;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
public class StoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}
}

@Slf4j
@Configuration
class CloudConfig {

	@Bean @LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	@ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true", matchIfMissing = true)
	ApplicationRunner discoveryExample(DiscoveryClient client, ItemsClient items) {
		return args -> {
			log.info("Warehouse instances found:");
			client.getInstances("warehouse").forEach(serviceInstance -> {
				log.info(" - {}", serviceInstance.getUri());
			});

			log.info("Warehouse items found:");
			items.findAll().forEach(item -> log.info("{}", item));
		};
	}
}

@Slf4j
@Configuration
class ClientsConfig {

	@Bean
	ItemsClient itemsClient(RestTemplate rest) {
		return new ItemsClient() {
			@Override
			public List<ItemRepresentation> findAll() {
				ParameterizedTypeReference<List<ItemRepresentation>> responseType =
						new ParameterizedTypeReference<List<ItemRepresentation>>() {};
				List<ItemRepresentation> items = rest
						.exchange(
								"http://warehouse/items",
								HttpMethod.GET,
								null,
								responseType)
						.getBody();
				items.stream().findFirst().ifPresent(item -> {
					log.info("Got all items from instance {}", item.getInstanceId());
				});
				return items;
			}

			@Override
			public ItemRepresentation findOne(long id) {
				ItemRepresentation item = rest
						.getForEntity("http://warehouse/items/{id}", ItemRepresentation.class, id)
						.getBody();
				log.info("Got item from instance {}", item.getInstanceId());
				return item;
			}

			@Override
			public void updateStock(ItemStockUpdate changes) {

			}
		};
	}

	@Bean
	SpecialClient specialClient() {
		return new SpecialClient() {
			@Override
			public SpecialCalculation calculateFor(long itemId, SpecialCalculationRequest request) {
				return new SpecialCalculation(null, request.getUnitPrice().multiply(BigDecimal.valueOf(request.getUnitCount())));
			}
		};
	}
}
