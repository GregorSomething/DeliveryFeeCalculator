package me.gregors.ratecalc.data;

/**
 * Enum to store possible cities that  can be used as input.
 */
public enum City {
    TALLINN("Tallinn-Harku"),
    TARTU("Tartu-Tõravere"),
    PARNU("Pärnu");

    private final String weatherSationName;

    City(String weatherSationName) {
        this.weatherSationName = weatherSationName;
    }

    /**
     * Returns weather station name for that city, (wmocode was not set for all stations)
     * @return station name, in that weather api.
     */
    public String getWeatherSationName() {
        return weatherSationName;
    }
}
