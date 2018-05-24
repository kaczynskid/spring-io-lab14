package io.spring.lab.warehouse.item;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@EnableBinding(ItemsBinding.class)
public class ItemEndpoint {

    private final ItemService items;

    @StreamListener(ItemsBinding.STOCK_UPDATE)
    public void updateStock(ItemStockUpdate changes) {
        log.info("Update item stock with changes: {}", changes);
        items.updateStock(changes);
    }
}
