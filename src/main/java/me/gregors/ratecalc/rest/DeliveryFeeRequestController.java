package me.gregors.ratecalc.rest;

import me.gregors.ratecalc.data.DeliveryFeeRequest;
import me.gregors.ratecalc.data.DeliveryFeeRequestBody;
import me.gregors.ratecalc.data.DeliveryFeeResponse;
import me.gregors.ratecalc.manager.DeliveryFeeManager;
import me.gregors.ratecalc.rules.BusinessRules;
import me.gregors.ratecalc.rules.CalculationRuleException;
import me.gregors.ratecalc.weather.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class DeliveryFeeRequestController {
    private final DeliveryFeeManager manager;

    @Autowired
    public DeliveryFeeRequestController(DeliveryFeeManager manager) {
        this.manager = manager;
    }

    /**
     * Endpoint for delivery fee calculation, REST post.
     * @param requestBody request inputs
     * @return calculated delivery fee, or error.
     * @throws CalculationRuleException if business rules stated that error should be given
     * @throws IllegalArgumentException if request had missing or invalid data
     */
    @PostMapping("/api/delivery_fee")
    public DeliveryFeeResponse calculateFee(@RequestBody DeliveryFeeRequestBody requestBody)
            throws CalculationRuleException, IllegalArgumentException {
        return this.manager.getResponseFor(requestBody);
    }
}
