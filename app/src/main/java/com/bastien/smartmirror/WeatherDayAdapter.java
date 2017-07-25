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

public class WeatherDayAdapter extends BaseAdapter {

    ArrayList<WeatherForecastDto> myList = new ArrayList<WeatherForecastDto>();
    Context context;

    // on passe le context afin d'obtenir un LayoutInflater pour utiliser notre
    // row_layout.xml
    // on passe les valeurs de notre à l'adapter
    public WeatherDayAdapter(Context context, ArrayList<WeatherForecastDto> myList) {
        this.myList = myList;
        this.context = context;
    }

    // retourne le nombre d'objet présent dans notre liste
    @Override
    public int getCount() {
        return myList.size();
    }

    // retourne un élément de notre liste en fonction de sa position
    @Override
    public WeatherForecastDto getItem(int position) {
        return myList.get(position);
    }

    // retourne l'id d'un élément de notre liste en fonction de sa position
    @Override
    public long getItemId(int position) {
        return myList.indexOf(getItem(position));
    }

    // retourne la vue d'un élément de la liste
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder = null;

        // au premier appel ConvertView est null, on inflate notre layout
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.weather_week, parent, false);

            // nous plaçons dans notre MyViewHolder les vues de notre layout
            mViewHolder = new MyViewHolder();
            mViewHolder.textViewDay = (TextView) convertView
                    .findViewById(R.id.day);
            mViewHolder.textViewTemperature = (TextView) convertView
                    .findViewById(R.id.minMaxTemperature);
            mViewHolder.imageViewIcon = (ImageView) convertView
                    .findViewById(R.id.iconDay);

            // nous attribuons comme tag notre MyViewHolder à convertView
            convertView.setTag(mViewHolder);
        } else {
            // convertView n'est pas null, nous récupérons notre objet MyViewHolder
            // et évitons ainsi de devoir retrouver les vues à chaque appel de getView
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        // nous récupérons l'item de la liste demandé par getView
        WeatherForecastDto weatherForecastDto = (WeatherForecastDto) getItem(position);

        String minTemperature = String.format(Locale.FRANCE, "%d°",
                Math.round(weatherForecastDto.getdayMinTemperature()));
        String maxTemperature = String.format(Locale.FRANCE, "%d°",
                Math.round(weatherForecastDto.getdayMaxTemperature()));

        // nous pouvons attribuer à nos vues les valeurs de l'élément de la liste
        mViewHolder.textViewDay.setText(weatherForecastDto.getDay());
        mViewHolder.textViewTemperature.setText(minTemperature + "/" + maxTemperature);
        mViewHolder.imageViewIcon.setImageResource(weatherForecastDto.getIcon());

        // nous retournos la vue de l'item demandé
        return convertView;
    }

    // MyViewHolder va nous permettre de ne pas devoir rechercher
    // les vues à chaque appel de getView, nous gagnons ainsi en performance
    private class MyViewHolder {
        TextView textViewDay, textViewTemperature;
        ImageView imageViewIcon;
    }
}
