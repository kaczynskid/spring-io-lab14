package io.spring.lab.warehouse.item;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ItemsBinding {

    String STOCK_UPDATE = "stockUpdate";

    @Input(STOCK_UPDATE)
    SubscribableChannel stockUpdate();

}
