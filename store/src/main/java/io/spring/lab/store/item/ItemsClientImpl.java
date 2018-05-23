package io.spring.lab.store.item;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
class ItemsClientImpl implements ItemsClient {

    @FeignClient(name = "warehouse", path = "/items")
    interface FeignItemsClient {

        @GetMapping
        List<ItemRepresentation> findAll();

        @GetMapping("/{id}")
        ItemRepresentation findOne(@PathVariable("id") long id);

        @PutMapping("/{id}/stock")
        void updateStock(@PathVariable("id")long id, @RequestBody ItemStockUpdate changes);
    }

    private final FeignItemsClient client;

    @Override
    public List<ItemRepresentation> findAll() {
        List<ItemRepresentation> items = client.findAll();
        items.stream().findFirst().ifPresent(item -> {
            log.info("Feign got all items from instance {}", item.getInstanceId());
        });
        return items;
    }

    @Override
    public ItemRepresentation findOne(long id) {
        ItemRepresentation item = client.findOne(id);
        log.info("Feign got item from instance {}", item.getInstanceId());
        return item;
    }

    @Override
    public void updateStock(ItemStockUpdate changes) {
        client.updateStock(changes.getId(), changes);
    }
}
