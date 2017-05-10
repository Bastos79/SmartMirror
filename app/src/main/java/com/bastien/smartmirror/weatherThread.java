package com.bastien.smartmirror;

/**
 * Created by Bastien on 06/05/2017.
 */
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.bastien.smartmirror.dto.weatherDto;
import com.bastien.smartmirror.Network;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Weather extends DataUpdater<weatherDto>{

    private static final String TAG = Weather.class.getSimpleName();

    /**
     * The time in milliseconds between API calls to update the weather.
     */
    private static final long UPDATE_INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(5);

    /**
     * The context used to load string resources.
     */
    private final Context context;

    public weatherDto weatherDto;

    /**
     * The current longitude.
     */
    private Double longitude;

    /**
     * The current latitude.
     */
    private Double latitude;

    /**
     * When creating a new {@link DataUpdater}, provide a non-{@code null} {@link UpdateListener} and
     * an update interval in milliseconds.
     *
     * @param updateListener
     * @param updateIntervalMillis
     */
    public Weather(UpdateListener<com.bastien.smartmirror.dto.weatherDto> updateListener, long updateIntervalMillis) {
        super(updateListener, UPDATE_INTERVAL_MILLIS);
        this.context = context;
    }

    @Override
    protected weatherDto getData() {

        weatherDto.getInstance();

        longitude = weatherDto.getCityLongitude();
        latitude = weatherDto.getCityLatitude();

        // Get the latest data from the Dark Sky API.
        String requestUrl = getRequestUrl();

        // Parse the data we are interested in from the response JSON.
        try {
            JSONObject response = Network.getJson(requestUrl);
            if (response != null) {
                return new WeatherData(
                        parseCurrentTemperature(response),
                        parseCurrentPrecipitationProbability(response),
                        parseDaySummary(response),
                        parseDayPrecipitationProbability(response),
                        parseCurrentIcon(response),
                        parseDayIcon(response));
            } else {
                return null;
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse weather JSON.", e);
            return null;
        }
    }

    /**
     * Creates the URL for a Dark Sky API request based on the specified {@link Location} or
     * {@code null} if the location is unknown.
     */
    private String getRequestUrl() {
            return String.format(Locale.FRANCE, "https://api.darksky.net/forecast/%s/%f,%f",
                    context.getString(R.string.dark_sky_api_key),
                    latitude,
                    longitude);
    }

    @Override
    protected String getTag() {
        return TAG;
    }

}
