package me.gregors.ratecalc.weather;

import me.gregors.ratecalc.data.City;
import me.gregors.ratecalc.weather.data.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * Used to store weather data
 */
public interface WeatherRepository extends JpaRepository<Weather, Integer> {

    /**
     * Gets latest weather data in DB by station name
     * @param station station name by what to search data
     * @return latest weather data
     */
    Weather getFirstByStationOrderByTimestampDesc(String station);

    Weather getFirstByStationAndTimestampBeforeOrderByTimestampDesc(String station, long timestamp);

    /**
     * Gets latest weather data in DB for specified city
     * @param city for what to get latest weather
     * @return latest weather
     */
    default Weather getLatestFor(City city) {
        return this.getFirstByStationOrderByTimestampDesc(city.getWeatherSationName());
    }

    default Weather getForAtTime(City city, long timestamp) {
        return this.getFirstByStationAndTimestampBeforeOrderByTimestampDesc(city.getWeatherSationName(), timestamp);
    }
}
