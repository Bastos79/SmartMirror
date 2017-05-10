package com.bastien.smartmirror;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bastien.smartmirror.dto.weatherDto;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by bmilcend on 05/07/2016.
 */

public class placeAutoComplete extends AsyncTask<String, Integer, String> implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private EditTextPreference mCity;
    private ArrayList<String> mPlaces;
    private AutocompletePredictionBuffer autocompletePredictions;
    public weatherDto weatherDto;

    public placeAutoComplete(Context context, EditTextPreference city){
        mContext = context;
        mCity = city;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("SmartM", "Google client before"  + mContext);

        mGoogleApiClient = new GoogleApiClient
                .Builder(mContext)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        Log.i("SmartM", "Google client after"  + mGoogleApiClient);
        //Get weather object
        weatherDto.getInstance();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.preference_city);
        builder = builder.setAdapter(new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, mPlaces),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String[] place = mPlaces.get(which).split(",");
                                mCity.setSummary(place[0]);
                                String placeId = autocompletePredictions.get(which).getPlaceId();

                                Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                                            @Override
                                            public void onResult(PlaceBuffer places) {
                                                if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                                    final Place myPlace = places.get(0);

                                                    Log.i("SmartM", "Place found: " + myPlace.getName() + " Long : " +
                                                            myPlace.getLatLng().longitude + " Lat : " + myPlace.getLatLng().latitude);
                                                    weatherDto.setCityName(myPlace.getName().toString());
                                                    weatherDto.setCityLatitude(myPlace.getLatLng().latitude);
                                                    weatherDto.setCityLongitude(myPlace.getLatLng().longitude);

                                                } else {
                                                    Log.e("SmartM", "Place not found");
                                                }
                                                places.release();
                                            }
                                        });

                            }
                        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected String doInBackground(String... params) {
        Log.i("SmartM", "Google client background"  + mGoogleApiClient);
        AutocompleteFilter autoCompleteFilter = new AutocompleteFilter.Builder().setTypeFilter(5).build();
        PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, params[0], null, autoCompleteFilter);
        autocompletePredictions = result.await(60, TimeUnit.SECONDS);
        final com.google.android.gms.common.api.Status status = autocompletePredictions.getStatus();
        if (!status.isSuccess()) {
            //Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
            //        Toast.LENGTH_SHORT).show();
            Log.e("SmartM", "Error getting autocomplete prediction API call: " + status.toString());
            autocompletePredictions.release();
            return null;
        }

        Log.i("SmartM", "Query completed. Received " + autocompletePredictions.getCount()
                + " predictions.");
        Log.i("SmartM", "Prediction 1 " + autocompletePredictions.get(0).getPrimaryText(null) + "," +
                autocompletePredictions.get(0).getSecondaryText(null));
        Log.i("SmartM", "Prediction 2 " + autocompletePredictions.get(1).getPrimaryText(null) + "," +
                autocompletePredictions.get(1).getSecondaryText(null));
        Log.i("SmartM", "Prediction 3 " + autocompletePredictions.get(2).getPrimaryText(null) + "," +
                autocompletePredictions.get(2).getSecondaryText(null));

        if (autocompletePredictions.getCount() > 0)
        {
            mPlaces = new ArrayList<String>();
        }
        for (int i = 0; i < 3; i++)
        {
            if (autocompletePredictions.getCount() >= i)
            {
                mPlaces.add(autocompletePredictions.get(i).getPrimaryText(null) + ", " +
                        autocompletePredictions.get(i).getSecondaryText(null));
            }
        }

        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
