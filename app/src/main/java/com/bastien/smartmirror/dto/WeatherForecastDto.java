package com.bastien.smartmirror.dto;

/**
 * Created by Bastien on 02/04/2017.
 */

public class WeatherForecastDto {

    /** Weather Now Temperature*/
    private Double currentTemperature = null;
    /** Weather Precipitation Probability*/
    private Double precipitationProba = null;
    /** Weather Current Icon*/
    private Integer currentIcon = null;

    /** Constructor */
    public WeatherForecastDto() {
    }

    /** Instance unique non préinitialisée */
    private static WeatherForecastDto mWeatherForecastDto = null;

    /** Point d'accès pour l'instance unique du singleton */
    public static synchronized WeatherForecastDto getInstance()
    {
        if (mWeatherForecastDto == null)
        { 	mWeatherForecastDto = new WeatherForecastDto();
        }
        return mWeatherForecastDto;
    }

    public double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(double currentTemperature) {
        this.currentTemperature = currentTemperature;
    }

    public double getPrecipitationProba() {
        return precipitationProba;
    }

    public void setPrecipitationProba(double precipitationProba) {
        this.precipitationProba = precipitationProba;
    }

    public Integer getCurrentIcon() {
        return currentIcon;
    }

    public void setCurrentIcon(Integer currentIcon) {
        this.currentIcon = currentIcon;
    }
}
