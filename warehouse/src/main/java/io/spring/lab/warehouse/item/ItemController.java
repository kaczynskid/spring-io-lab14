package io.spring.lab.warehouse.item;

import static io.spring.lab.warehouse.error.ErrorMessage.messageOf;
import static io.spring.lab.warehouse.error.ErrorMessage.messageResponseOf;
import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.net.URI;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.MeterRegistry;
import io.spring.lab.warehouse.error.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService items;

    private final MeterRegistry registry;

    private final Environment env;

    @GetMapping
    List<ItemRepresentation> findAll() {
        registry.counter("web.items.get").increment();
        return items.findAll().stream()
                .map(ItemRepresentation::of)
                .map(i -> i.withInstanceId(env.getRequiredProperty("info.instanceId")))
                .collect(toList());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ItemRepresentation request) {
        registry.counter("web.items.post").increment();
        Item item = items.create(request.asItem());
        return ResponseEntity.created(selfUriOf(item)).build();
    }

    private static URI selfUriOf(Item item) {
        return linkTo(methodOn(ItemController.class).findOne(item.getId())).toUri();
    }

    @GetMapping("/{id}")
    public ItemRepresentation findOne(@PathVariable("id") long id) {
        registry.counter("web.items.id.get").increment();
        return ItemRepresentation.of(items.findOne(id)).
                withInstanceId(env.getRequiredProperty("info.instanceId"));
    }

    @PutMapping("/{id}")
    public ItemRepresentation update(@PathVariable("id") long id, @RequestBody ItemUpdate changes) {
        registry.counter("web.items.id.put").increment();
        return ItemRepresentation.of(items.update(changes.withId(id)));
    }

    @PutMapping("/{id}/stock")
    public ItemRepresentation updateStock(@PathVariable("id") long id, @RequestBody ItemStockUpdate changes) {
        registry.counter("web.items.id.stock.put").increment();
        return ItemRepresentation.of(items.updateStock(changes.withId(id)));
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorMessage handle(ItemNotFound e) {
        return messageOf(e);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handle(OutOfStock e) {
        return messageResponseOf(BAD_REQUEST, e);
    }
}
