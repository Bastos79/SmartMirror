package com.bastien.smartmirror;

/**
 * Created by Bastien on 06/05/2017.
 */
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.bastien.smartmirror.dto.WeatherDto;
import com.bastien.smartmirror.dto.WeatherForecastDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Weather extends DataUpdater<WeatherForecastDto>{

    private static final String TAG = Weather.class.getSimpleName();

    /**
     * The time in milliseconds between API calls to update the weather.
     */
    private static final long UPDATE_INTERVAL_MILLIS = TimeUnit.MINUTES.toMillis(5);

    /**
     * The context used to load string resources.
     */
    private final Context context;

    /**
     * The weatherDto object.
     */
    public WeatherDto weatherDto;

    /**
     * The weatherDto object.
     */
    public WeatherForecastDto weatherForecastDto;

    /**
     * The current longitude.
     */
    private Double longitude;

    /**
     * The current latitude.
     */
    private Double latitude;

    /**
     * A {@link Map} from Dark Sky's icon code to the corresponding drawable resource ID.
     */
    private final Map<String, Integer> iconResources = new HashMap<String, Integer>() {{
        put("clear-day", R.drawable.clear_day);
        put("clear-night", R.drawable.clear_night);
        put("cloudy", R.drawable.cloudy);
        put("fog", R.drawable.fog);
        put("partly-cloudy-day", R.drawable.partly_cloudy_day);
        put("partly-cloudy-night", R.drawable.partly_cloudy_night);
        put("rain", R.drawable.rain);
        put("sleet", R.drawable.sleet);
        put("snow", R.drawable.snow);
        put("wind", R.drawable.wind);
    }};

    /**
     * When creating a new {@link DataUpdater}, provide a non-{@code null} {@link UpdateListener} and
     * an update interval in milliseconds.
     *
     * @param updateListener
     * @param context
     */
    public Weather(Context context, UpdateListener<WeatherForecastDto> updateListener) {
        super(updateListener, UPDATE_INTERVAL_MILLIS);
        this.context = context;
    }

    @Override
        protected WeatherForecastDto getData() {

        weatherDto = weatherDto.getInstance();
        //TODO changer en objet classique
        weatherForecastDto = weatherForecastDto.getInstance();

        longitude = weatherDto.getCityLongitude();
        latitude = weatherDto.getCityLatitude();

        // Get the latest data from the Dark Sky API.
        String requestUrl = getRequestUrl();

        // Parse the data we are interested in from the response JSON.
        try {
            JSONObject response = Network.getJson(requestUrl);
            if (response != null) {
                        parseCurrentTemperature(response);
                        parseCurrentPrecipitationProbability(response);
                        parseDaySummary(response);
                        parseDayPrecipitationProbability(response);
                        parseCurrentIcon(response);
                        parseDayIcon(response);
                        return weatherForecastDto;
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
            return String.format(Locale.FRANCE, "https://api.darksky.net/forecast/%s/%f,%f?lang=fr&units=auto",
                    context.getString(R.string.dark_sky_api_key),
                    latitude,
                    longitude);
    }

    /**
     * Reads the current temperature from the API response. API documentation:
     * https://darksky.net/dev/docs
     */
    private void parseCurrentTemperature(JSONObject response) throws JSONException {
        JSONObject currently = response.getJSONObject("currently");
        weatherForecastDto.setCurrentTemperature(currently.getDouble("temperature"));
    }

    /**
     * Reads the current precipitation probability from the API response. API documentation:
     * https://darksky.net/dev/docs
     */
    private void parseCurrentPrecipitationProbability(JSONObject response) throws JSONException {
        JSONObject currently = response.getJSONObject("currently");
        weatherForecastDto.setPrecipitationProba(currently.getDouble("precipProbability"));
    }

    /**
     * Reads the 24-hour forecast summary from the API response. API documentation:
     * https://darksky.net/dev/docs
     */
    private String parseDaySummary(JSONObject response) throws JSONException {
        JSONObject hourly = response.getJSONObject("hourly");
        return hourly.getString("summary");
    }

    /**
     * Reads the 24-hour forecast precipitation probability from the API response. API documentation:
     * https://darksky.net/dev/docs
     */
    private Double parseDayPrecipitationProbability(JSONObject response) throws JSONException {
        JSONObject hourly = response.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        // Calculate the average over the whole day.
        double sum = 0;
        for (int i = 0; i < data.length(); i++) {
            double probability = data.getJSONObject(i).getDouble("precipProbability");
            sum += probability;
        }
        return sum / data.length();
    }

    /**
     * Reads the current weather icon code from the API response. API documentation:
     * https://darksky.net/dev/docs
     */
    private void parseCurrentIcon(JSONObject response) throws JSONException {
        JSONObject currently = response.getJSONObject("currently");
        String icon = currently.getString("icon");
        weatherForecastDto.setCurrentIcon(iconResources.get(icon));
    }

    /**
     * Reads the 24-hour forecast weather icon code from the API response. API documentation:
     * https://darksky.net/dev/docs
     */
    private Integer parseDayIcon(JSONObject response) throws JSONException {
        JSONObject hourly = response.getJSONObject("hourly");
        String icon = hourly.getString("icon");
        return iconResources.get(icon);
    }

    @Override
    protected String getTag() {
        return TAG;
    }

}
