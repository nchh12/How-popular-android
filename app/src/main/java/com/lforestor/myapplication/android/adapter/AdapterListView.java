package com.lforestor.myapplication.android.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.lforestor.myapplication.android.R;
import com.lforestor.myapplication.android.repo.FieldEnums;
import com.lforestor.myapplication.android.repo.WordsRepo;
import com.lforestor.myapplication.android.utils.JSONParam;

import java.util.ArrayList;
import java.util.List;

public class AdapterListView extends BaseAdapter {
    Context context;
    List<String> arrayWordString;
    LayoutInflater layoutInflater;
    int[] backgroundColors;

    public AdapterListView(Context context, List<String> arrayWordString, int[] backgroundColors) {
        this.context = context;
        this.arrayWordString = arrayWordString;
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

    int getColor(int percent) {
        if (percent >= 80) {
            return backgroundColors[5];
        } else if (percent >= 70) {
            return backgroundColors[4];
        } else if (percent >= 60) {
            return backgroundColors[3];
        } else if (percent >= 50) {
            return backgroundColors[2];
        } else if (percent >= 30) {
            return backgroundColors[1];
        } else {
            return backgroundColors[0];
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        Log.d("@@@",position+" "+convertView);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_listview, null);
            viewHolder = new ViewHolder();
            //define
            viewHolder.wordString = convertView.findViewById(R.id.wordString);
            viewHolder.wordPoint = convertView.findViewById(R.id.wordPoint);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.math_tapping);
            viewHolder.wordString.setTypeface(typeface);
            viewHolder.wordPoint.setTypeface(typeface);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JSONParam data = new JSONParam(arrayWordString.get(arrayWordString.size() - 1 - position));
        String word = data.getFieldSafely(FieldEnums.word);
        Double rate = Double.parseDouble(data.getFieldSafely(FieldEnums.frequency));
        String results = data.getFieldSafely(FieldEnums.results);
//        ArrayList<String> test = data.getFieldResultsArray(FieldEnums.synonyms, 0);
//        String test2 = data.getFieldResults(FieldEnums.definition, 0);
//        Log.d("@@@", results);
//        Log.d("@@@", test.get(0));
//        Log.d("@@@", test2);
        int frequencyPoint = (int) (rate / WordsRepo.MAX_FREQUENCY_POINT * 100);

        viewHolder.wordString.setText(word);
        viewHolder.wordPoint.setText(frequencyPoint + "%");

        //color
        int colorHash = getColor(frequencyPoint);
        viewHolder.wordPoint.setTextColor(colorHash);
        viewHolder.wordString.setTextColor(colorHash);
        return convertView;
    }

    static class ViewHolder {
        TextView wordString;
        TextView wordPoint;
    }
}
