package com.bastien.smartmirror;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bastien.smartmirror.dto.WeatherDto;
import com.bastien.smartmirror.dto.WeatherForecastDto;

import java.util.Locale;

public class ActivityMirror extends AppCompatActivity {

    private TextView temperatureView;
    private TextView weatherSummaryView;
    private TextView precipitationView;
    private ImageView iconView;

    private Weather weather;

    /**
     * The listener used to populate the UI with weather data.
     */
    private final DataUpdater.UpdateListener<WeatherForecastDto> weatherUpdateListener =
            new DataUpdater.UpdateListener<WeatherForecastDto>() {
                @Override
                public void onUpdate(WeatherForecastDto data) {
                    if (data != null) {

                        // Populate the current temperature rounded to a whole number.
                        String temperature = String.format(Locale.FRANCE, "%d°",
                                Math.round(data.getCurrentTemperature()));
                        temperatureView.setText(temperature);

                        // Populate the 24-hour forecast summary, but strip any period at the end.
                        //String summary = util.stripPeriod(data.daySummary);
                        //weatherSummaryView.setText(summary);

                        // Populate the precipitation probability as a percentage rounded to a whole number.
                        String precipitation =
                                String.format(Locale.FRANCE, "%d%%", Math.round(100 * data.getPrecipitationProba()));
                        precipitationView.setText(precipitation);

                        // Populate the icon for the current weather.
                        iconView.setImageResource(data.getCurrentIcon());

                        // Show all the views.
                        temperatureView.setVisibility(View.VISIBLE);
                        weatherSummaryView.setVisibility(View.VISIBLE);
                        precipitationView.setVisibility(View.VISIBLE);
                        iconView.setVisibility(View.VISIBLE);
                    } else {

                        // Hide everything if there is no data.
                        temperatureView.setVisibility(View.GONE);
                        weatherSummaryView.setVisibility(View.GONE);
                        precipitationView.setVisibility(View.GONE);
                        iconView.setVisibility(View.GONE);
                    }

                }
            };

                    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);
                        temperatureView = (TextView) findViewById(R.id.temperature);
                        weatherSummaryView = (TextView) findViewById(R.id.weather_summary);
                        precipitationView = (TextView) findViewById(R.id.precipitation);
                        iconView = (ImageView) findViewById(R.id.icon);

                        weather = new Weather(this, weatherUpdateListener);
    }

    public void onStart() {
        super.onStart();
        weather.start();

    }

    @Override
    protected void onStop() {
        weather.stop();
        super.onStop();
    }
}
