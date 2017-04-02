package com.bastien.smartmirror.dto;

/**
 * Created by Bastien on 02/04/2017.
 */

public class weatherDto {
    /** City Name*/
    private String cityName;
    /** City Longitude*/
    private String cityLongitude;
    /** City Latitude*/
    private String cityLatitude;

    /** Constructor */
    public weatherDto() {
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

