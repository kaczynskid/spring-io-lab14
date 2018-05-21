package io.spring.lab.warehouse.item;

import static io.spring.lab.warehouse.TestDataConfiguration.itemsTestData;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @MockBean
    ItemService items;

    @Autowired
    MockMvc mvc;

    @Test
    public void shouldGetAllItems() throws Exception {
        doReturn(itemsTestData())
                .when(items).findAll();

        mvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.[0].name").value("A"))
                .andExpect(jsonPath("$.[0].price").value("40.0"));
    }

    @Test
    public void shouldCreateItem() throws Exception {
        long itemId = 5L;
        Item item = new Item(null, "test", 5, BigDecimal.valueOf(13.5));

        doAnswer(returnItemWithSetId(itemId))
                .when(items).create(item);

        mvc.perform(post("/items").contentType(APPLICATION_JSON_UTF8)
                .content("{\"name\": \"test\", \"count\": 5, \"price\": 13.5}"))
                .andExpect(status().isCreated());
    }

    private static Answer returnItemWithSetId(long itemId) {
        return invocation -> {
            Item item = invocation.getArgument(0);
            writeField(item, "id", itemId, true);
            return item;
        };
    }
}
