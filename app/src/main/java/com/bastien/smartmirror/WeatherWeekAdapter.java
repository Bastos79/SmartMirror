package com.bastien.smartmirror;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bastien.smartmirror.dto.WeatherForecastDto;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Bastien on 16/07/2017.
 */

public class WeatherWeekAdapter extends RecyclerView.Adapter<WeatherWeekAdapter.ViewHolder> {

    ArrayList<WeatherForecastDto> myList = new ArrayList<WeatherForecastDto>();
    Context context;

    // on passe le context afin d'obtenir un LayoutInflater pour utiliser notre
    // row_layout.xml
    // on passe les valeurs de notre à l'adapter
    public WeatherWeekAdapter(Context context, ArrayList<WeatherForecastDto> myList) {
        this.myList = myList;
        this.context = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textViewDay, textViewTemperature;
        public ImageView imageViewIcon;
        public ViewHolder(View view) {
            super(view);
            this.textViewDay = (TextView) view
                    .findViewById(R.id.day);
            this.textViewTemperature = (TextView) view
                    .findViewById(R.id.minMaxTemperature);
            this.imageViewIcon = (ImageView) view
                    .findViewById(R.id.iconDay);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WeatherWeekAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_week, null);
        WeatherWeekAdapter.ViewHolder viewHolder = new WeatherWeekAdapter.ViewHolder(view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        WeatherForecastDto weatherForecastDto = (WeatherForecastDto) getItem(position);

        String minTemperature = String.format(Locale.FRANCE, "%d°",
                Math.round(weatherForecastDto.getdayMinTemperature()));
        String maxTemperature = String.format(Locale.FRANCE, "%d°",
                Math.round(weatherForecastDto.getdayMaxTemperature()));

        // nous pouvons attribuer à nos vues les valeurs de l'élément de la liste
        holder.textViewDay.setText(weatherForecastDto.getDay());
        holder.textViewTemperature.setText(minTemperature + "/" + maxTemperature);
        holder.imageViewIcon.setImageResource(weatherForecastDto.getIcon());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myList.size();
    }


    // retourne un élément de notre liste en fonction de sa position
    public WeatherForecastDto getItem(int position) {
        return myList.get(position);
    }

    // retourne l'id d'un élément de notre liste en fonction de sa position
    @Override
    public long getItemId(int position) {
        return myList.indexOf(getItem(position));
    }
}
