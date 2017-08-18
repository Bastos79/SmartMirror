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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
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
    private String longitude;

    /**
     * The current latitude.
     */
    private String latitude;

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
        put("dash", R.drawable.dash);
    }};

    /**
     * A {@link Map} from Dark Sky's time to the corresponding day name.
     */
    private final Map<Integer, String> dayName = new HashMap<Integer, String>() {{
        put(2, "Lun");
        put(3, "Mar");
        put(4, "Mer");
        put(5, "Jeu");
        put(6, "Ven");
        put(7, "Sam");
        put(1, "Dim");

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
                        parseWeekForecast(response);
                        parseDaySummary(response);
                        parseCurrentIcon(response);
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
            return String.format(Locale.FRANCE, "https://api.darksky.net/forecast/%s/%s,%s?lang=fr&units=auto",
                    context.getString(R.string.dark_sky_api_key),
                    latitude,
                    longitude);
    }

    /**
     * Reads the current temperature from the API response. API documentation:
     * https://darksky.net/dev/docs
     */
    private void parseWeekForecast(JSONObject response) throws JSONException {
        ArrayList<WeatherForecastDto> weekWeather = new ArrayList<WeatherForecastDto>();
        ArrayList<WeatherForecastDto> hourWeather = new ArrayList<WeatherForecastDto>();

        JSONObject currently = response.getJSONObject("currently");
        JSONObject daily = response.getJSONObject("daily");
        JSONArray dailyDatas = daily.getJSONArray("data");
        JSONObject hourly = response.getJSONObject("hourly");
        JSONArray hourlyDatas = hourly.getJSONArray("data");


        int dayNumber = dailyDatas.length();
        for (int i = 1; i < dayNumber; i++)
        {
            JSONObject weatherDay = dailyDatas.getJSONObject(i);
            Calendar cal = new GregorianCalendar();

            WeatherForecastDto tempWeatherForecastDto = new WeatherForecastDto();
            double maxTemperature = weatherDay.getDouble("temperatureMax");
            double minTemperature = weatherDay.getDouble("temperatureMin");
            Long time = weatherDay.getLong("time") * 1000;
            String icon = weatherDay.getString("icon");

            tempWeatherForecastDto.setdayMaxTemperature(maxTemperature);
            tempWeatherForecastDto.setdayMinTemperature(minTemperature);
            cal.setTimeInMillis(time);
            tempWeatherForecastDto.setDay(dayName.get(cal.get(Calendar.DAY_OF_WEEK)));
            tempWeatherForecastDto.setIcon(iconResources.get(icon));
            weekWeather.add(tempWeatherForecastDto);
        }

        String previousIcon = "";
        for (int i = 0; i < 12; i++)
        {
            JSONObject weatherHour = hourlyDatas.getJSONObject(i);
            Calendar cal = new GregorianCalendar();

            WeatherForecastDto tempWeatherForecastDto = new WeatherForecastDto();
            double temperature = weatherHour.getDouble("temperature");
            Long time = weatherHour.getLong("time") * 1000;
            String icon = weatherHour.getString("icon");
            if(previousIcon.equals(icon))
            {
                icon = "dash";
            }
            previousIcon = icon;

            tempWeatherForecastDto.setCurrentTemperature(temperature);
            cal.setTimeInMillis(time);
            StringBuilder hourTime = new StringBuilder();
            hourTime.append(cal.get(Calendar.HOUR_OF_DAY));
            hourTime.append("h");
            tempWeatherForecastDto.setHour(hourTime.toString());
            tempWeatherForecastDto.setIcon(iconResources.get(icon));
            hourWeather.add(tempWeatherForecastDto);
        }

        weatherForecastDto.setWeatherWeek(weekWeather);
        weatherForecastDto.setWeatherHour(hourWeather);

        weatherForecastDto.setdayMinTemperature(weekWeather.get(0).getdayMinTemperature());
        weatherForecastDto.setdayMaxTemperature(weekWeather.get(0).getdayMaxTemperature());
        weatherForecastDto.setCurrentTemperature(currently.getDouble("temperature"));
    }

    /**
     * Reads the 24-hour forecast summary from the API response. API documentation:
     * https://darksky.net/dev/docs
     */
    private void parseDaySummary(JSONObject response) throws JSONException {
        JSONObject hourly = response.getJSONObject("hourly");
        weatherForecastDto.setDaySummary(hourly.getString("summary"));
    }

    /**
     * Reads the current weather icon code from the API response. API documentation:
     * https://darksky.net/dev/docs
     */
    private void parseCurrentIcon(JSONObject response) throws JSONException {
        JSONObject currently = response.getJSONObject("currently");
        String icon = currently.getString("icon");
        weatherForecastDto.setIcon(iconResources.get(icon));
    }



    @Override
    protected String getTag() {
        return TAG;
    }

}
