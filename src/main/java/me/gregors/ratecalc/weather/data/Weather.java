package me.gregors.ratecalc.weather.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

/**
 * Class that represents weather measurements at specific time and place
 */
@Entity @Table(name = "weather_mesurments")
@NoArgsConstructor(force = true) @Getter
public class Weather {

    @Column(name = "timestamp_seconds", nullable = false)
    private final long timestamp;

    // I Tried to put it to a different object to save only one, but I did not find out how to avoid its session issues and primary key issues,
    //   using SQL querys I would have just inserted if it was missing.
    //@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    //@JoinColumn(name = "station_id", nullable = false)
    //private final Station station;
    @Column(name = "station_name")
    private final String station;

    @Column(name = "station_wmo")
    private final int WMO;

    @Column(name = "temperature_celsus", nullable = false)
    private final double temperature;

    @Column(name = "windspeed_ms", nullable = false)
    private final double windspeed;

    @Column(name = "phenomenon")
    private final WeatherPhenomenon phenomenon;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public Weather(long timestamp, String station, int stationWMO, double temperature, double windspeed, WeatherPhenomenon phenomenon) {
        this.timestamp = timestamp;
        this.station = station;
        this.WMO = stationWMO;
        this.temperature = temperature;
        this.windspeed = windspeed;
        this.phenomenon = phenomenon;
    }

    public Weather(long timestamp, String name, String wmocode, String temp, String windspeed, String phenomenon) {
        this(timestamp, name, Integer.parseInt(wmocode),
                Double.parseDouble(temp), Double.parseDouble(windspeed),
                phenomenon == null ? WeatherPhenomenon.UNKNOWN : WeatherPhenomenon.valueOf(
                        phenomenon.toUpperCase(Locale.ROOT).replaceAll(" ", "_")));
    }
}
