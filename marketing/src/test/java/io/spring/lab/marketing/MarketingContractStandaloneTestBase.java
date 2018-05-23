package io.spring.lab.marketing;

import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.spring.lab.marketing.special.SpecialController;
import io.spring.lab.marketing.special.SpecialService;
import io.spring.lab.marketing.special.calculate.SpecialCalculation;

@RunWith(SpringRunner.class)
public abstract class MarketingContractStandaloneTestBase {

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(new SpecialController(stubSpecialService()));
    }

    private static SpecialService stubSpecialService() {
        SpecialService specials = Mockito.mock(SpecialService.class);
        doReturn(new SpecialCalculation(null, BigDecimal.valueOf(90)))
                .when(specials).calculateFor(5L, BigDecimal.valueOf(30), 3);
        doReturn(new SpecialCalculation("abcdefghijklmnopqrstuw0123456789", BigDecimal.valueOf(150)))
                .when(specials).calculateFor(1L, BigDecimal.valueOf(40), 5);
        return specials;
    }

}
