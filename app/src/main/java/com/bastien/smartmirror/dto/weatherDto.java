package com.bastien.smartmirror.dto;

/**
 * Created by Bastien on 02/04/2017.
 */

public class weatherDto {

    /** City Name*/
    private String cityName = null;
    /** City Longitude*/
    private double cityLongitude;
    /** City Latitude*/
    private double cityLatitude;

    /** Constructor */
    public weatherDto() {
    }

    /** Instance unique non préinitialisée */
    private static weatherDto mWeatherDto = null;

    /** Point d'accès pour l'instance unique du singleton */
    public static synchronized weatherDto getInstance()
    {
        if (mWeatherDto == null)
        { 	mWeatherDto = new weatherDto();
        }
        return mWeatherDto;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getCityLongitude() {
        return cityLongitude;
    }

    public void setCityLongitude(double cityLongitude) {
        this.cityLongitude = cityLongitude;
    }

    public double getCityLatitude() {
        return cityLatitude;
    }

    public void setCityLatitude(double cityLatitude) {
        this.cityLatitude = cityLatitude;
    }
}

