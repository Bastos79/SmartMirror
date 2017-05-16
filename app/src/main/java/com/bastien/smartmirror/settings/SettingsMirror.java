package com.bastien.smartmirror.settings;

import android.content.Intent;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bastien.smartmirror.ActivityMirror;
import com.bastien.smartmirror.PlaceAutoComplete;
import com.bastien.smartmirror.R;

import java.util.List;

public class SettingsMirror extends PreferenceActivity  {

       @Override
       protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isValidFragment("GeneralFragment");
        isValidFragment("WeatherFragment");
        isValidFragment("MapFragment");

        // Add a button to the header list.
        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("Démarrer le mirroir");
            setListFooter(button);

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),ActivityMirror.class);
                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
       return true;
    }

    //Populate the activity with the top-level headers.
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    public static class GeneralFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
            //PreferenceManager.setDefaultValues(getActivity(),R.xml.advanced_preferences, false);

            // Load the preferences from an XML resource
            //PreferenceManager.setDefaultValues(getActivity(),R.xml.general_preferences, false);
            addPreferencesFromResource(R.xml.general_preferences);
        }
    }

    public static class WeatherFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.weather_preferences);

            final SwitchPreference mSwitchWeather = (SwitchPreference) findPreference("activate_weather_preference");
            final CheckBoxPreference mGeolocalisation = (CheckBoxPreference) findPreference("geoloc_weather_preference");
            final EditTextPreference mCity = (EditTextPreference) findPreference("city_weather_preference");

            //Test if the Geolocalisation is activated
            if (mGeolocalisation.isChecked()) {
                mCity.setEnabled(false);
            } else {
                mCity.setEnabled(true);
                if (mCity.getText() == null)
                {
                    mCity.setSummary(R.string.header_weather_summary);
                    Log.i("SmartM", "No CITY");
                }
                else
                {
                    mCity.setSummary(mCity.getText());
                    Log.i("SmartM", "GETTEXT CITY : " + mCity.getText() + " UPDATE SUMMARY");
                    Log.i("SmartM", "GETEDITTEXT CITY : " + mCity.getEditText().getText() + " UPDATE SUMMARY");
                }
                Log.i("SmartM", "INIT OK");
            }

            mGeolocalisation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (mGeolocalisation.isChecked()) {
                        mCity.setEnabled(false);
                    } else {
                        mCity.setEnabled(true);
                    }
                    return true;
                }
            });

            mCity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {

                    new PlaceAutoComplete(getActivity(), mCity).execute(mCity.getEditText().getText().toString());

                    /*
                        new Thread(new Runnable() {
                            public void run() {
                    PendingResult<AutocompletePredictionBuffer>  result = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, mCity.getText(), null, null);
                    AutocompletePredictionBuffer autocompletePredictions = result.await(60, TimeUnit.SECONDS);
                    //PendingResult<PlaceBuffer>  result = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, mCity.getText(), null, null);
                    //PlaceBuffer autocompletePlace = result.await(3, TimeUnit.SECONDS);
                    Log.e("GPS CITY ID",autocompletePredictions.get(0).getPlaceId());
                    Log.e("GPS CITY",autocompletePredictions.get(0).toString());
                            }
                        }).start();
                    */
                    return true;
                }
            });
        }
    }

    public static class MapFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.map_preferences);

            final SwitchPreference mSwitchWeather = (SwitchPreference) findPreference("activate_map_preference");
            final CheckBoxPreference mGeolocalisation = (CheckBoxPreference) findPreference("geoloc_map_preference");
            final EditTextPreference mCity = (EditTextPreference) findPreference("city_map_preference");

            //Test if the Geolocalisation is activated
            if (mGeolocalisation.isChecked()) {
                mCity.setEnabled(false);
            } else {
                mCity.setEnabled(true);
            }

            mGeolocalisation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                  if (mGeolocalisation.isChecked()) {
                  mCity.setEnabled(false);
                  } else {
                       mCity.setEnabled(true);
                  }
                  return true;
                    }
            });
        }
    }

    public static class TransportFragment extends PreferenceFragment {

    }

    public static class Prefs1Fragment extends PreferenceFragment {

    }


}