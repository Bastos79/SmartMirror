package com.bastien.smartmirror.dto;

/**
 * Created by Bastien on 02/04/2017.
 */

public class WeatherForecastDto {

    /** Weather Now Temperature*/
    private Double currentTemperature = null;
    /** Weather Now Temperature*/
    private Double dayMaxTemperature = null;
    /** Weather Now Temperature*/
    private Double dayMinTemperature = null;
    /** Weather Precipitation Probability*/
    private Double precipitationProba = null;
    /** Weather Current Icon*/
    private Integer currentIcon = null;
    /** Weather Day Summary*/
    private String daySummary = null;

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

    public double getdayMinTemperature() {
        return dayMinTemperature;
    }

    public void setdayMinTemperature(double dayMinTemperature) {
        this.dayMinTemperature = dayMinTemperature;
    }

    public double getdayMaxTemperature() {
        return dayMaxTemperature;
    }

    public void setdayMaxTemperature(double dayMaxTemperature) {
        this.dayMaxTemperature = dayMaxTemperature;
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

    public String getDaySummary() {
        return daySummary;
    }

    public void setDaySummary(String daySummary) {
        this.daySummary = daySummary;
    }
}
