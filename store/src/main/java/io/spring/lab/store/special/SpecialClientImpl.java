package io.spring.lab.store.special;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SpecialClientImpl implements SpecialClient {

    @FeignClient(name = "marketing", path = "/specials", fallback = FallbackSpecialsClient.class)
    interface FeignSpecialsClient {

        @PostMapping("/{itemId}/calculate")
        SpecialCalculation calculateFor(@PathVariable("itemId") long itemId, @RequestBody SpecialCalculationRequest request);
    }

    @Component
    static class FallbackSpecialsClient implements FeignSpecialsClient {

        @Override
        public SpecialCalculation calculateFor(long itemId, SpecialCalculationRequest request) {
            return new SpecialCalculation(null, request.getUnitPrice().multiply(BigDecimal.valueOf(request.getUnitCount())));
        }
    }

    private final FeignSpecialsClient specials;

    @Override
    public SpecialCalculation calculateFor(long itemId, SpecialCalculationRequest request) {
        return specials.calculateFor(itemId, request);
    }
}
