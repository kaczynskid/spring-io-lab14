package io.spring.lab.store.basket;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.math.BigDecimal;

import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties.StubsMode;
import org.springframework.test.annotation.DirtiesContext;

import io.spring.lab.store.SpringTestBase;
import io.spring.lab.store.basket.item.BasketItem;
import io.spring.lab.store.basket.item.BasketItemService;
import io.spring.lab.store.item.ItemRepresentation;
import io.spring.lab.store.item.ItemsClient;

@SpringBootTest(webEnvironment = NONE)
@AutoConfigureStubRunner(
        stubsMode = StubsMode.LOCAL,
        ids = "io.spring.lab:marketing:+"
)
@DirtiesContext
public class BasketServiceTest extends SpringTestBase {

    @MockBean ItemsClient items;

    @Autowired BasketService baskets;
    @Autowired BasketItemService basketItems;

    @Test
    public void shouldUpdateBasketWithRegularPriceItem() {
        // given
        Basket basket = baskets.create();
        long itemId = 5L;
        int unitCount = 3;
        BigDecimal unitPrice = BigDecimal.valueOf(30.0);
        givenItemExists(itemId, unitPrice);

        // when
        BasketUpdateDiff diff = baskets.updateItem(basket.getId(), itemId, unitCount);

        // then
        assertThat(diff.getCountDiff()).isEqualTo(3);
        assertThat(diff.getPriceDiff()).isEqualByComparingTo(BigDecimal.valueOf(90.0));
        BasketItem basketItem = basketItems.findOneItem(basket.getId(), itemId);
        assertThat(basketItem.getName()).isEqualTo("E");
        assertThat(basketItem.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(90.0));
        assertThat(basketItem.getSpecialId()).isNull();
    }

    @Test
    public void shouldUpdateBasketWithSpecialPriceItem() {
        // given
        Basket basket = baskets.create();
        long itemId = 1L;
        int unitCount = 5;
        BigDecimal unitPrice = BigDecimal.valueOf(40.0);
        givenItemExists(itemId, unitPrice);

        // when
        BasketUpdateDiff diff = baskets.updateItem(basket.getId(), itemId, unitCount);

        // then
        assertThat(diff.getCountDiff()).isEqualTo(5);
        assertThat(diff.getPriceDiff()).isEqualByComparingTo(BigDecimal.valueOf(150.0));
        BasketItem basketItem = basketItems.findOneItem(basket.getId(), itemId);
        assertThat(basketItem.getName()).isEqualTo("E");
        assertThat(basketItem.getTotalPrice()).isEqualByComparingTo(BigDecimal.valueOf(150.0));
        assertThat(basketItem.getSpecialId()).isEqualTo("abcdefghijklmnopqrstuw0123456789");
    }

    private void givenItemExists(long itemId, BigDecimal unitPrice) {
        when(items.findOne(itemId))
                .thenReturn(new ItemRepresentation("E", unitPrice));
    }
}
