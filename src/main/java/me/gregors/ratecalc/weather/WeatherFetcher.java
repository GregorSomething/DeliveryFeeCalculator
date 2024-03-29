package me.gregors.ratecalc.weather;

import me.gregors.ratecalc.data.City;
import me.gregors.ratecalc.weather.data.Weather;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class WeatherFetcher {
    private static final Logger logger = Logger.getLogger("weather-fetcher");
    private static final String URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";

    /**
     * Gets weather data from "<a href="https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php">API</a>"
     * @param cities cities for what you want weather data
     * @return List of weather data for those cities
     * @throws IOException parser errors
     */
    public List<Weather> getFor(City... cities) throws IOException {
        Set<String> stations = Arrays.stream(cities)
                .map(City::getWeatherSationName).collect(Collectors.toSet());
        try {
            Element root = XMLRequestHelper.getRootOf(URL);
            return this.process(root, stations);
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private List<Weather> process(Element root, Set<String> stations) {
        List<Element> elements = XMLRequestHelper.getElementNodes(root.getChildNodes());
        long timestamp = Long.parseLong(root.getAttribute("timestamp"));
        return filterAndCreate(elements, stations, timestamp);
    }

    private List<Weather> filterAndCreate(List<Element> elements, Set<String> stations, long timestamp) {
        return elements.stream()
                .filter(e -> stations.contains(XMLRequestHelper.getNodeContent("name", e)))
                .map(e -> this.elementToWeather(e, timestamp))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Weather elementToWeather(Element element, long timestamp) {
        String name = XMLRequestHelper.getNodeContent("name", element);
        String wmoCodeStr = XMLRequestHelper.getNodeContent("wmocode", element);
        String temperatureStr = XMLRequestHelper.getNodeContent("airtemperature", element);
        String windspeedStr = XMLRequestHelper.getNodeContent("windspeed", element);
        String phenomenonStr = XMLRequestHelper.getNodeContent("phenomenon", element);
        try {
            return new Weather(timestamp, name, wmoCodeStr, temperatureStr, windspeedStr, phenomenonStr);
        } catch (NumberFormatException e) {
            logger.severe("Failed to create Weather object, number was not numeric");
            return null;
        }
    }


}
