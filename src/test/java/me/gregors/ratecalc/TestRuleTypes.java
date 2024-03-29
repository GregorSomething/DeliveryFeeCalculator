package me.gregors.ratecalc;


import me.gregors.ratecalc.data.City;
import me.gregors.ratecalc.data.DeliveryFeeRequest;
import me.gregors.ratecalc.data.VehicleType;
import me.gregors.ratecalc.rules.BusinessRules;
import me.gregors.ratecalc.rules.types.CalculationRule;
import me.gregors.ratecalc.rules.CalculationRuleException;
import me.gregors.ratecalc.weather.data.Weather;
import me.gregors.ratecalc.weather.data.WeatherPhenomenon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static me.gregors.ratecalc.rules.types.CalculationRule.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestRuleTypes {

    public static void assertRule(CalculationRule<Integer, Integer> rule, int in, int out) {
        try {
            assertEquals(rule.apply(in), out);
        } catch (CalculationRuleException ignored) {}
    }

    @Test
    public void testConstant() {
        assertRule(constant(20), 0, 20);
    }

    @Test
    public void testException() {
        assertThrows(CalculationRuleException.class, () -> error(t -> new Exception()).apply(0));
    }

    @Test
    public void testIf() {
        assertRule(ifThen(i -> i == 10, constant(0), constant(1)), 10, 0);
        assertRule(ifThen(i -> i == 10, constant(0), constant(1)), 2, 1);
    }

    @Test
    public void testMap() {
        CalculationRule<Integer, Integer> map = map(i -> switch (i) {
            case 1 -> 100;
            case 2 -> 200;
            default -> 3;
        });
        assertRule(map, 1, 100);
        assertRule(map, 2, 200);
        assertRule(map, 4, 3);
    }

    @Test
    public void testMaxOf() {
        assertRule(max(constant(1), constant(2), constant(3)), 0, 3);
        assertRule(max(constant(1)), 2, 1);
    }

    @Test
    public void testReduce() {
        assertRule(reduce(Integer::sum, constant(1)), 0, 1);
        assertRule(reduce(Integer::sum, constant(1), constant(2), constant(3)), 0, 6);
    }

    @Test
    public void testTransform() {
        assertRule(transform(i -> i * 2, constant(4)), 0, 8);
    }

    @Test
    public void testBuissnesRules() throws CalculationRuleException {
        Weather normal = new Weather(0, "a", 1, 10, 5, WeatherPhenomenon.CLEAR);
        DeliveryFeeRequest r1 = new DeliveryFeeRequest(normal, City.TALLINN, VehicleType.BIKE);
        DeliveryFeeRequest r2 = new DeliveryFeeRequest(normal, City.PARNU, VehicleType.CAR);
        DeliveryFeeRequest r3 = new DeliveryFeeRequest(normal, City.TARTU, VehicleType.SCOOTER);
        Assertions.assertEquals(BusinessRules.calculateFeeFor(r1).deliveryFee(), 3.0);
        assertEquals(BusinessRules.calculateFeeFor(r2).deliveryFee(), 3.0);
        assertEquals(BusinessRules.calculateFeeFor(r3).deliveryFee(), 3.0);
    }

    @Test
    public void testBuissnesRules2() throws CalculationRuleException {
        Weather normal = new Weather(0, "a", 1, 10, 5, WeatherPhenomenon.CLEAR);
        Weather temp1 = new Weather(0, "a", 1, -1, 5, WeatherPhenomenon.CLEAR);
        Weather temp2 = new Weather(0, "a", 1, -11, 5, WeatherPhenomenon.CLEAR);
        Weather wind1 = new Weather(0, "a", 1, 10, 11, WeatherPhenomenon.CLEAR);
        Weather wind2 = new Weather(0, "a", 1, 10, 21, WeatherPhenomenon.CLEAR);

        DeliveryFeeRequest r1 = new DeliveryFeeRequest(normal, City.TALLINN, VehicleType.BIKE);
        DeliveryFeeRequest r2 = new DeliveryFeeRequest(temp1, City.TALLINN, VehicleType.BIKE);
        DeliveryFeeRequest r3 = new DeliveryFeeRequest(temp2, City.TALLINN, VehicleType.BIKE);
        DeliveryFeeRequest r4 = new DeliveryFeeRequest(wind1, City.TALLINN, VehicleType.BIKE);
        DeliveryFeeRequest r5 = new DeliveryFeeRequest(wind2, City.TALLINN, VehicleType.BIKE);

        assertEquals(3.0, BusinessRules.calculateFeeFor(r1).deliveryFee());
        assertEquals(3.5, BusinessRules.calculateFeeFor(r2).deliveryFee());
        assertEquals(4.0, BusinessRules.calculateFeeFor(r3).deliveryFee());
        assertEquals(3.5, BusinessRules.calculateFeeFor(r4).deliveryFee());
        assertThrows(CalculationRuleException.class, () -> BusinessRules.calculateFeeFor(r5));
    }

    @Test
    public void testWeatherPhenomenon() throws CalculationRuleException {
        for (WeatherPhenomenon p : WeatherPhenomenon.values()) {
            Weather w1 = new Weather(0, "a", 1, 10, 5, p);
            DeliveryFeeRequest r1 = new DeliveryFeeRequest(w1, City.TALLINN, VehicleType.BIKE);
            DeliveryFeeRequest r2 = new DeliveryFeeRequest(w1, City.TALLINN, VehicleType.CAR);
            DeliveryFeeRequest r3 = new DeliveryFeeRequest(w1, City.TALLINN, VehicleType.SCOOTER);
            if (p.isDangerousForBike()) {
                assertThrows(CalculationRuleException.class, () -> BusinessRules.calculateFeeFor(r1));
                assertThrows(CalculationRuleException.class, () -> BusinessRules.calculateFeeFor(r3));
            } else if (p.isSnowing()) {
                assertEquals(4.0, BusinessRules.calculateFeeFor(r1).deliveryFee());
                assertEquals(4.5, BusinessRules.calculateFeeFor(r3).deliveryFee());
            } else if (p.isRaining()) {
                assertEquals(3.5, BusinessRules.calculateFeeFor(r1).deliveryFee());
                assertEquals(4.0, BusinessRules.calculateFeeFor(r3).deliveryFee());
            } else {
                assertEquals(3.0, BusinessRules.calculateFeeFor(r1).deliveryFee());
                assertEquals(3.5, BusinessRules.calculateFeeFor(r3).deliveryFee());
            }
            assertEquals(4.0, BusinessRules.calculateFeeFor(r2).deliveryFee());
        }
    }
}
