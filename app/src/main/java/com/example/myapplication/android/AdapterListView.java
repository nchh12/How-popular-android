package com.example.myapplication.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

public class AdapterListView extends BaseAdapter {
    Context context;
    List<String> arrayWordString;
    List<Integer> arrayWordPoint;
    LayoutInflater layoutInflater;
    int[] backgroundColors;
    AdapterListView(Context context, List<String> arrayWordString, List<Integer> arrayWordPoint, int[] backgroundColors){
        this.context = context;
        this.arrayWordString = arrayWordString;
        this.arrayWordPoint = arrayWordPoint;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.backgroundColors = backgroundColors;
    }
    @Override
    public int getCount() {
        return arrayWordString.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    int getColor(int percent){
        if (percent >= 80) {
            return backgroundColors[5];
        }else if (percent >= 70){
            return backgroundColors[4];
        }else if (percent >= 60){
            return backgroundColors[3];
        }else if (percent >= 50){
            return backgroundColors[2];
        }else if (percent >= 30){
            return backgroundColors[1];
        }else{
            return backgroundColors[0];
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        Log.d("@@@",position+" "+convertView);
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.activity_listview, null);
            viewHolder = new ViewHolder();
            //define
            viewHolder.wordString = convertView.findViewById(R.id.wordString);
            viewHolder.wordPoint = convertView.findViewById(R.id.wordPoint);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.math_tapping);
            viewHolder.wordString.setTypeface(typeface);
            viewHolder.wordPoint.setTypeface(typeface);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.wordString.setText(arrayWordString.get(arrayWordString.size()-1-position));
        viewHolder.wordPoint.setText(arrayWordPoint.get(arrayWordString.size()-1-position)+"%");
        //color
        int colorHash = getColor(arrayWordPoint.get(arrayWordString.size()-1-position));
        viewHolder.wordPoint.setTextColor(colorHash);
        viewHolder.wordString.setTextColor(colorHash);
        return convertView;
    }
    static class ViewHolder{
        TextView wordString;
        TextView wordPoint;
    }
}
