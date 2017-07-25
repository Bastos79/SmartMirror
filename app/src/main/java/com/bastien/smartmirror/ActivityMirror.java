package com.bastien.smartmirror;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bastien.smartmirror.dto.WeatherDto;
import com.bastien.smartmirror.dto.WeatherForecastDto;
import com.google.android.gms.vision.text.Text;

import java.util.Locale;

public class ActivityMirror extends AppCompatActivity {

    private TextView temperatureView;
    private TextView weatherSummaryView;
    private TextView windView;
    private ImageView iconView;
    private RecyclerView weatherWeekView;


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
                        String currentTemperature = String.format(Locale.FRANCE, "%d°",
                                Math.round(data.getCurrentTemperature()));
                        temperatureView.setText(currentTemperature);

                        // Populate the 24-hour forecast summary, but strip any period at the end.
                        String summary = removeDot(data.getDaySummary());
                        weatherSummaryView.setText(summary);

                        // Populate the precipitation probability as a percentage rounded to a whole number.
                        //String precipitation =
                        //        String.format(Locale.FRANCE, "%d%%", Math.round(100 * data.getPrecipitationProba()));
                        //windView.setText(precipitation);

                        // Populate the icon for the current weather.
                        iconView.setImageResource(data.getIcon());

                        // Show all the views.
                        temperatureView.setVisibility(View.VISIBLE);
                        weatherSummaryView.setVisibility(View.VISIBLE);
                        windView.setVisibility(View.VISIBLE);
                        iconView.setVisibility(View.VISIBLE);
                    } else {

                        // Hide everything if there is no data.
                        temperatureView.setVisibility(View.GONE);
                        weatherSummaryView.setVisibility(View.GONE);
                        windView.setVisibility(View.GONE);
                        iconView.setVisibility(View.GONE);
                    }

                    //Show the weather week
                    WeatherWeekAdapter adapter = new WeatherWeekAdapter(getApplicationContext(), data.getWeatherweek());
                    weatherWeekView.setAdapter(adapter);

                }
            };

                    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);
                        temperatureView = (TextView) findViewById(R.id.temperature);
                        weatherSummaryView = (TextView) findViewById(R.id.weather_summary);
                        windView = (TextView) findViewById(R.id.wind);
                        iconView = (ImageView) findViewById(R.id.icon);
                        //weatherWeekView = (ListView) findViewById(R.id.weatherWeek);
                        LinearLayoutManager layoutManager
                                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

                        weatherWeekView = (RecyclerView) findViewById(R.id.weatherWeek);
                        weatherWeekView.setLayoutManager(layoutManager);

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

    /**
     * Removes the period from the end of a sentence, if there is one.
     */
    public String removeDot(String sentence) {
        if (sentence == null) {
            return null;
        }
        if ((sentence.length() > 0) && (sentence.charAt(sentence.length() - 1) == '.')) {
            return sentence.substring(0, sentence.length() - 1);
        } else {
            return sentence;
        }
    }
}
