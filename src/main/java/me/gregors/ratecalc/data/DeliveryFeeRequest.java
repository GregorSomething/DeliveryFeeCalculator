package me.gregors.ratecalc.data;

import me.gregors.ratecalc.weather.WeatherRepository;
import me.gregors.ratecalc.weather.data.Weather;

import java.util.Date;
import java.util.Locale;

/**
 * Class to delivery fee calculation info
 * @param weather weather for that request
 * @param city for what city the request is for
 * @param vehicle vehicleType
 */
public record DeliveryFeeRequest(Weather weather, City city, VehicleType vehicle) {

    /**
     * Makes request object from given data
     * @param repository for getting weather info
     * @param body containing necessary parameters from Rest API request
     * @return object with parsed values and weather data
     */
    public static DeliveryFeeRequest of(WeatherRepository repository, DeliveryFeeRequestBody body) {
        if (body.city() == null || body.vehicle() == null)
            throw new IllegalArgumentException("Request must have parameters city and vehicle.");

        City c = City.valueOf(body.city().toUpperCase(Locale.ROOT));
        VehicleType v = VehicleType.valueOf(body.vehicle().toUpperCase(Locale.ROOT));

        if (body.atTimestamp() == null)
            return new DeliveryFeeRequest(repository.getLatestFor(c), c, v);
        return DeliveryFeeRequest.ofTime(repository, c, v, body.atTimestamp());
    }

    private static DeliveryFeeRequest ofTime(WeatherRepository repository, City c, VehicleType v, Long timestamp) {
        if (timestamp >= new Date().getTime() / 1000)
            throw new IllegalArgumentException("Timestamp is from future. No weather record.");

        Weather w = repository.getForAtTime(c, timestamp);

        if (w == null)
            throw new IllegalArgumentException("No data for timestamp: " + timestamp);
        return new DeliveryFeeRequest(w, c, v);
    }
}
