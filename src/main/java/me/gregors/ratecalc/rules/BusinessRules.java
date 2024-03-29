package me.gregors.ratecalc.rules;

import me.gregors.ratecalc.data.DeliveryFeeRequest;
import me.gregors.ratecalc.data.DeliveryFeeResponse;
import me.gregors.ratecalc.data.VehicleType;
import me.gregors.ratecalc.rules.types.CalculationRule;

import static me.gregors.ratecalc.rules.types.CalculationRule.*;
public class BusinessRules {
    private static final CalculationRule<DeliveryFeeRequest, Double> RBF =
            reduce(Double::sum,
                    map(t -> switch (t.city()) {
                        case TALLINN -> 3.0;
                        case TARTU -> 2.5;
                        case PARNU -> 2.0;
                    }),
                    map(t -> switch (t.vehicle()) {
                        case CAR -> 1.0;
                        case SCOOTER -> 0.5;
                        case BIKE -> 0.0;
                    }));
    
    private static final CalculationRule<DeliveryFeeRequest, Double> AREF =
            ifThen(r -> r.vehicle() == VehicleType.BIKE || r.vehicle() == VehicleType.SCOOTER,
                    ifThen(r -> r.weather().getTemperature() <= 0,
                            ifThen(r -> r.weather().getTemperature() < -10,
                                    constant(1.0),
                                    constant(0.5)),
                            constant(0.0)),
                    constant(0.0));
    private static final CalculationRule<DeliveryFeeRequest, Double> WSEF =
            ifThen(r -> r.vehicle() == VehicleType.BIKE,
                    ifThen(r -> r.weather().getWindspeed() >= 10,
                            ifThen(r -> r.weather().getWindspeed() > 20,
                                    error(r -> new Exception("Usage of selected vehicle " +
                                            "type is forbidden")),
                                    constant(0.5)),
                            constant(0.0)),
                    constant(0.0));
    private static final CalculationRule<DeliveryFeeRequest, Double> WPEF =
            ifThen(r -> (r.vehicle() == VehicleType.BIKE || r.vehicle() == VehicleType.SCOOTER),
                    max(
                            ifThen(r -> r.weather().getPhenomenon().isSnowing(),
                                    constant(1.0),
                                    constant(0.0)),
                            ifThen(r -> r.weather().getPhenomenon().isRaining(),
                                    constant(0.5),
                                    constant(0.0)),
                            ifThen(r -> r.weather().getPhenomenon().isDangerousForBike(),
                                    error(r -> new Exception("Usage of selected vehicle type is forbidden")),
                                    constant(0.0))),
                    constant(0.0)
            );


    private static final CalculationRule<DeliveryFeeRequest, Double> ALL_RULES =
            reduce(Double::sum, RBF, AREF, WSEF, WPEF);

    /**
     * Calculates delivery fee on business rules.
     * @param request object that rules accept, and what contains necessary business info.
     * @return calculated delivery fee in its wrapper object
     * @throws CalculationRuleException if error rule got called
     */
    public static DeliveryFeeResponse calculateFeeFor(DeliveryFeeRequest request) throws CalculationRuleException {
        return new DeliveryFeeResponse(
                ALL_RULES.apply(request));
    }
}
