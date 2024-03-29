package me.gregors.ratecalc.weather.data;

/**
 * Enum that represents all possible weather phenomenons, according to API doc
 */
public enum WeatherPhenomenon {
    CLEAR(false, false, false),
    FEW_CLOUDS(false, false, false),
    VARIABLE_CLOUDS(false, false, false),
    CLOUDY_WITH_CLEAR_SPEELS(false, false, false),
    OVERCAST(false, false, false),
    LIGHT_SNOW_SHOWER(true, false, false),
    MODERATE_SNOW_SHOWER(true, false, false),
    HEAVY_SNOW_SHOWER(true, false, false),
    LIGHT_SHOWER(false, true, false),
    MODERATE_SHOWER(false, true, false),
    HEAVY_SHOWER(false, true, false),
    LIGHT_RAIN(false, true, false),
    MODERATE_RAIN(false, true, false),
    HEAVY_RAIN(false, true, false),
    GLAZE(false, false, true),
    LIGHT_SLEET(true, false, false),
    MODERATE_SLEET(true, false, false),
    LIGHT_SNOWFALL(true, false, false),
    MODERATE_SNOWFALL(true, false, false),
    BLOWING_SNOW(true, false, false),
    DRIFTING_SNOW(true, false, false),
    HAIL(false, true, true),
    MIST(false, false, false),
    FOG(false, false, false),
    THUNDER(false, false, true),
    // isDangerousForBike could be true, but it was not mentioned in instructions.
    THUNDERSTORM(false, true, false),
    UNKNOWN(false, false, false);

    private final boolean isSnow;
    private final boolean isRain;
    private final boolean isDangerousForBike;

    WeatherPhenomenon(boolean isSnow, boolean isRain, boolean isDangerousForBike) {
        this.isSnow = isSnow;
        this.isRain = isRain;
        this.isDangerousForBike = isDangerousForBike;
    }

    /**
     * If it is snowing
     * @return true if it is snowing
     */
    public boolean isSnowing() {
        return isSnow;
    }

    /**
     * If it is raining
     * @return true if it is raining
     */
    public boolean isRaining() {
        return isRain;
    }

    /**
     * If it is dangerous for scooter or bike
     * @return true if is dangerous
     */
    public boolean isDangerousForBike() {
        return isDangerousForBike;
    }
}
