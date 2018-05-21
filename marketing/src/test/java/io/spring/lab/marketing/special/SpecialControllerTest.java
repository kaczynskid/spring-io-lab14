package io.spring.lab.marketing.special;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(SpecialController.class)
public class SpecialControllerTest {

    @MockBean
    SpecialService items;

    @Autowired
    MockMvc mvc;

    @Test
    public void shouldListAllSpecials() throws Exception {
        doReturn(singletonList(new Special("A", 1, 3, BigDecimal.valueOf(70))))
            .when(items).findAll();

        mvc.perform(MockMvcRequestBuilders.get("/specials"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].itemId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].batchSize").value("3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].batchPrice").value("70"));
    }
}
