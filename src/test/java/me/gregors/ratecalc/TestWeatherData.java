package me.gregors.ratecalc;

import me.gregors.ratecalc.data.City;
import me.gregors.ratecalc.weather.WeatherDataUpdater;
import me.gregors.ratecalc.weather.WeatherFetcher;
import me.gregors.ratecalc.weather.WeatherRepository;
import me.gregors.ratecalc.weather.data.Weather;
import me.gregors.ratecalc.weather.data.WeatherPhenomenon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TestWeatherData {

    private final WeatherRepository repository;
    private final WeatherFetcher weatherFetcher;

    @Autowired
    public TestWeatherData(WeatherRepository repository, WeatherFetcher weatherFetcher, WeatherDataUpdater updater) {
        this.repository = repository;
        this.weatherFetcher = weatherFetcher;
    }

    @Test
    public void testWeatherDataFetcher() throws IOException {
        List<Weather> wl = weatherFetcher.getFor(City.TALLINN);
        assertEquals(1, wl.size());
        Weather w = wl.get(0);
        assertEquals(26038, w.getWMO());
    }

    @Test
    public void testWeatherDataInRepoBeforeUpdate() {
        Weather w = repository.getLatestFor(City.TALLINN);
        assertEquals(26038, w.getWMO()); // If this is correct assume data was present
    }

    @Test
    public void testWeatherGetLatestAndSaveWorks() {
        Weather w = new Weather(Integer.MAX_VALUE, City.TALLINN.getWeatherSationName(),
                26038, 0.0, 0.0, WeatherPhenomenon.CLEAR);
        repository.saveAndFlush(w);
        Weather w1 = repository.getLatestFor(City.TALLINN);
        assertEquals(w.getTimestamp(), w1.getTimestamp());
        assertEquals(w.getWMO(), w1.getWMO());
        assertEquals(w.getTemperature(), w1.getTemperature());
        assertEquals(w.getWindspeed(), w1.getWindspeed());
        assertSame(w.getPhenomenon(), w1.getPhenomenon());
    }
}
