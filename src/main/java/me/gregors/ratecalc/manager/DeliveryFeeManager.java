package me.gregors.ratecalc.manager;

import me.gregors.ratecalc.data.*;
import me.gregors.ratecalc.rules.BusinessRules;
import me.gregors.ratecalc.rules.CalculationRuleException;
import me.gregors.ratecalc.weather.WeatherRepository;
import me.gregors.ratecalc.weather.data.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;

/**
 * Class that creates response to Delivery fee request.
 */
@Service
public class DeliveryFeeManager {

    private final WeatherRepository repository;

    @Autowired
    public DeliveryFeeManager(WeatherRepository repository) {
        this.repository = repository;
    }

    /**
     * Gets response to request
     * @param requestBody request parameters, as string
     * @return response to this request
     * @throws CalculationRuleException if business rules threw error
     * @throws IllegalArgumentException if request had invalid/missing data
     */
    public DeliveryFeeResponse getResponseFor(DeliveryFeeRequestBody requestBody) throws CalculationRuleException, IllegalArgumentException {
        DeliveryFeeRequest request = this.parseRequest(requestBody);
        return BusinessRules.calculateFeeFor(request);
    }

    private DeliveryFeeRequest parseRequest(DeliveryFeeRequestBody requestBody) {
        if (requestBody.city() == null || requestBody.vehicle() == null)
            throw new IllegalArgumentException("Request must have parameters city and vehicle.");

        City city = City.valueOf(requestBody.city().toUpperCase(Locale.ROOT));
        VehicleType vehicleType = VehicleType.valueOf(requestBody.vehicle().toUpperCase(Locale.ROOT));
        Weather weather = this.getWeatherFor(city, requestBody.atTimestamp());

        return new DeliveryFeeRequest(weather, city, vehicleType);
    }

    private Weather getWeatherFor(City city, Long timestamp) {
        if (timestamp == null)
            return this.repository.getLatestFor(city);

        if (timestamp >= new Date().getTime() / 1000)
            throw new IllegalArgumentException("Timestamp is from future. No weather record.");
        Weather weather = repository.getForAtTime(city, timestamp);
        if (weather == null)
            throw new IllegalArgumentException("No data for timestamp: " + timestamp);
        return weather;
    }
}
