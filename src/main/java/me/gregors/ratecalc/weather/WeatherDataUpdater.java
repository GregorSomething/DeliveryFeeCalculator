package me.gregors.ratecalc.weather;

import jakarta.annotation.PostConstruct;
import me.gregors.ratecalc.data.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class WeatherDataUpdater {
    private final WeatherFetcher weatherFetcher;
    private final WeatherRepository repository;
    private static final Logger logger = Logger.getLogger("weather-updater");

    @Autowired
    public WeatherDataUpdater(WeatherRepository repository, WeatherFetcher weatherFetcher) {
        this.repository = repository;
        this.weatherFetcher = weatherFetcher;
    }

    /**
     * Updates weather data at databsae, does this at startup and with cronjob, edit cronjob at application.properties
     */
    @PostConstruct // Post construct for initial data, so NPE would not be possible.
    @Async @Scheduled(cron = "${weather.update.cron}")
    public void update() {
        try {
            repository.saveAllAndFlush(weatherFetcher.getFor(City.values()));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to update weather info!", e);
        }
    }
}
