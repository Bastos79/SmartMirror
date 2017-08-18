package com.bastien.smartmirror.dto;

import java.util.ArrayList;

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
    /** Weather Icon*/
    private Integer icon = null;
    /** Weather Day*/
    private String day = null;
    /** Weather Hour*/
    private String hour = null;
    /** Weather Day Summary*/
    private String daySummary = null;
    /** Weather week forecast*/
    private ArrayList<WeatherForecastDto> weatherWeek = new ArrayList<WeatherForecastDto>();
    /** Weather week forecast*/
    private ArrayList<WeatherForecastDto> weatherHour = new ArrayList<WeatherForecastDto>();

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

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getDaySummary() {
        return daySummary;
    }

    public void setDaySummary(String daySummary) {
        this.daySummary = daySummary;
    }

    public ArrayList<WeatherForecastDto> getWeatherWeek() {
        return weatherWeek;
    }

    public void setWeatherWeek(ArrayList<WeatherForecastDto> weatherWeek) {
        this.weatherWeek = weatherWeek;
    }

    public ArrayList<WeatherForecastDto> getWeatherHour() {
        return weatherHour;
    }

    public void setWeatherHour(ArrayList<WeatherForecastDto> weatherHour) {
        this.weatherHour = weatherHour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
