package com.bastien.smartmirror.dto;

/**
 * Created by Bastien on 02/04/2017.
 */

public class WeatherDto {

    /** City Name*/
    private String cityName = null;
    /** City Longitude*/
    private String cityLongitude;
    /** City Latitude*/
    private String cityLatitude;

    /** Constructor */
    public WeatherDto() {
    }

    /** Instance unique non préinitialisée */
    private static WeatherDto mWeatherDto = null;

    /** Point d'accès pour l'instance unique du singleton */
    public static synchronized WeatherDto getInstance()
    {
        if (mWeatherDto == null)
        { 	mWeatherDto = new WeatherDto();
        }
        return mWeatherDto;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityLongitude() {
        return cityLongitude;
    }

    public void setCityLongitude(String cityLongitude) {
        this.cityLongitude = cityLongitude;
    }

    public String getCityLatitude() {
        return cityLatitude;
    }

    public void setCityLatitude(String cityLatitude) {
        this.cityLatitude = cityLatitude;
    }
}

