package me.gregors.ratecalc.data;

import me.gregors.ratecalc.weather.data.Weather;

/**
 * Class to delivery fee calculation info
 * @param weather weather for that request
 * @param city for what city the request is for
 * @param vehicle vehicleType
 */
public record DeliveryFeeRequest(Weather weather, City city, VehicleType vehicle) {

}
