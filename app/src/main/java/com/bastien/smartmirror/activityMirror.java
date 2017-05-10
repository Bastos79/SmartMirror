package com.bastien.smartmirror;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bastien.smartmirror.dto.weatherDto;

public class activityMirror extends AppCompatActivity {

    /**
     * The listener used to populate the UI with weather data.
     */
    private final DataUpdater.UpdateListener<weatherDto> weatherUpdateListener =
            new DataUpdater.UpdateListener<weatherDto>() {
                @Override
                public void onUpdate(weatherDto data) {
                    if (data != null) {

                    }
                }
            };

                    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mirror);
    }

    public void onStart() {
        super.onStart();

        Handler weatherHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //int progress=msg.getData().getInt(PROGRESS_BAR_INCREMENT);
            }
        };

    }


    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            Log.d("Handlers", "Called on main thread");
            // Repeat this the same runnable code block again another 2 seconds
            weatherHandler.postDelayed(runnableCode, 2000);
        }
    };
}
