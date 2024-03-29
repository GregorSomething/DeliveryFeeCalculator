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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestRestController {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WeatherRepository repository;

    @Test
    public void testValidData() throws Exception {
        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"city\": \"tallinn\", \"vehicle\": \"car\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void testInValidData() throws Exception {
        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"cit\": \"tallinn\", \"vehicle\": \"car\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string(containsString("Request must have parameters city and vehicle")));
        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"city\": \"tallinn\", \"vehicl\": \"car\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string(containsString("Request must have parameters city and vehicle")));
        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"city\": \"vanalinn\", \"vehicle\": \"car\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().string(containsString("No enum constant")));
    }

    @Test
    public void testPriceCalculator() throws Exception {
        Weather normal = new Weather(Integer.MAX_VALUE, City.TALLINN.getWeatherSationName(), 1, 10, 5, WeatherPhenomenon.CLEAR);
        repository.saveAndFlush(normal);
        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"city\": \"tallinn\", \"vehicle\": \"car\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().string(containsString("4.0")));
        repository.delete(normal);
    }

    @Test
    public void testPriceCalculatorException() throws Exception {
        Weather normal = new Weather(Integer.MAX_VALUE, City.TALLINN.getWeatherSationName(), 1, 10, 5, WeatherPhenomenon.GLAZE);
        repository.saveAndFlush(normal);
        this.mockMvc.perform(post("/api/delivery_fee")
                        .content("{\"city\": \"tallinn\", \"vehicle\": \"bike\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andExpect(content().string(containsString("Usage of selected vehicle type is forbidden")));
        repository.delete(normal);
    }
}
