package io.spring.lab.warehouse.item;

import static io.spring.lab.warehouse.item.QItem.item;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class JpaItemRepository implements ItemRepository {

    interface SpringDataItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

        Item findTopByOrderByPriceDesc();

        @Query("from Item order by price desc")
        List<Item> findMostExpensive(Pageable pageable);

        @Query("from Item where name like :prefix%")
        List<Item> findByNamePrefix(@Param("prefix") String namePrefix);

        @Override
        List<Item> findAll(Predicate predicate);
    }

    private final SpringDataItemRepository repository;

    @Override
    public Optional<Item> findOne(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Item> findAll() {
        return repository.findAll();
    }

    @Override
    public Item save(Item item) {
        return repository.save(item);
    }

    @Override
    public Item findMostExpensive() {
//        return repository.findTopByOrderByPriceDesc();

        return repository.findMostExpensive(PageRequest.of(0, 1)).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Empty DB!"));

    }

    @Override
    public List<Item> findByNamePrefix(String prefix) {
//        return repository.findByNamePrefix(prefix);
        
        return repository.findAll(withNameStartingWith(prefix));
    }

    private BooleanExpression withNameStartingWith(String prefix) {
        return item.name.startsWith(prefix);
    }
}
