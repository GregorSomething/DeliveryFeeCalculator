package me.gregors.ratecalc;

import me.gregors.ratecalc.data.City;
import me.gregors.ratecalc.weather.WeatherRepository;
import me.gregors.ratecalc.weather.data.Weather;
import me.gregors.ratecalc.weather.data.WeatherPhenomenon;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestControllerWithTimestamp {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WeatherRepository repository;

    @Test
    public void testInValidData() throws Exception {
        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"city\": \"tallinn\", \"vehicle\": \"car\", \"atTimestamp\": \"aab\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string(containsString("Failed to read request")));
    }

    @Test
    public void testNoWeatherData() throws Exception {
        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"city\": \"tallinn\", \"vehicle\": \"car\", \"atTimestamp\": \"1\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string(containsString("No data for timestamp")));
    }

    @Test
    public void testTimeFromFutureData() throws Exception {
        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"city\": \"tallinn\", \"vehicle\": \"car\", \"atTimestamp\": \"" + Integer.MAX_VALUE + "\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string(containsString("Timestamp is from future")));
    }

    @Test
    public void testValidTimestampValueData() throws Exception {
        Weather present = new Weather(new Date().getTime() / 1000 + 10, City.TALLINN.getWeatherSationName(), 1, 10, 5, WeatherPhenomenon.CLEAR);
        long timeInPast = new Date().getTime() / 1000 - 10000;
        Weather past = new Weather(timeInPast, City.TALLINN.getWeatherSationName(), 1, 10, 5, WeatherPhenomenon.GLAZE);

        repository.save(past);
        repository.saveAndFlush(present);

        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"city\": \"tallinn\", \"vehicle\": \"bike\", \"atTimestamp\": " + (timeInPast + 1) + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().string(containsString("Usage of selected vehicle type is forbidden")));
        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"city\": \"tallinn\", \"vehicle\": \"car\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().string(containsString("4.0")));
    }
}
