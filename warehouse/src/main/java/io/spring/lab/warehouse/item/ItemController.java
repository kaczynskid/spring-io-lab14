package io.spring.lab.warehouse.item;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService items;

    @GetMapping
    List<ItemRepresentation> findAll() {
        return items.findAll().stream()
                .map(ItemRepresentation::of)
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ItemRepresentation create(@RequestBody ItemRepresentation request) {
        return ItemRepresentation.of(items.create(request.asItem()));
    }

    @GetMapping("/{id}")
    public ItemRepresentation findOne(@PathVariable("id") long id) {
        return ItemRepresentation.of(items.findOne(id));
    }

    @PutMapping("/{id}")
    public ItemRepresentation update(@PathVariable("id") long id, @RequestBody ItemUpdate changes) {
        return ItemRepresentation.of(items.update(changes.withId(id)));
    }
}
