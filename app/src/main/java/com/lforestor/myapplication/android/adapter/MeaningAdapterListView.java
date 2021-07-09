package com.lforestor.myapplication.android.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.lforestor.myapplication.android.R;
import com.lforestor.myapplication.android.repo.FieldEnums;
import com.lforestor.myapplication.android.repo.WordsRepo;
import com.lforestor.myapplication.android.utils.JSONParam;

import java.util.ArrayList;
import java.util.List;

public class MeaningAdapterListView extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int[] backgroundColors;
    String string;
    int num;

    public MeaningAdapterListView (Context context, String string, int[] backgroundColors, int num){
        this.context = context;
        this.string = string;
        this.layoutInflater = LayoutInflater.from(context);
        this.backgroundColors = backgroundColors;
        this.num = num;
    }

    @Override
    public int getCount() {
        return num;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MeaningAdapterListView.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_listview_dictionary, null);
            viewHolder = new MeaningAdapterListView.ViewHolder();
            //define
            viewHolder.wordNumber = convertView.findViewById(R.id.meaningNumber);
            viewHolder.wordType = convertView.findViewById(R.id.meaningWordType);
            viewHolder.wordDefinition = convertView.findViewById(R.id.meaningDefinition);
            viewHolder.wordExample = convertView.findViewById(R.id.meaningExample);
            viewHolder.wordSynonym = convertView.findViewById(R.id.meaningSynonym);

            viewHolder.labelExample = convertView.findViewById(R.id.meaningExampleLabel);
            viewHolder.labelSynonym = convertView.findViewById(R.id.meaningSynonymLabel);
            viewHolder.lineExample = convertView.findViewById(R.id.meaningExampleLine);
            viewHolder.lineSynonym = convertView.findViewById(R.id.meaningSynonymLine);

            Typeface typeface = ResourcesCompat.getFont(context, R.font.math_tapping);
            viewHolder.wordType.setTypeface(typeface);
            viewHolder.labelExample.setTypeface(typeface);
            viewHolder.labelSynonym.setTypeface(typeface);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MeaningAdapterListView.ViewHolder) convertView.getTag();
        }
        JSONParam data = new JSONParam(string);
        String type = data.getFieldResults(FieldEnums.partOfSpeech, position);
        String definition = data.getFieldResults(FieldEnums.definition, position);
        ArrayList<String> examples = data.getFieldResultsArray(FieldEnums.examples, position);
        ArrayList<String> synonyms = data.getFieldResultsArray(FieldEnums.synonyms, position);
        Double rate = Double.parseDouble(data.getFieldSafely(FieldEnums.frequency));
        int frequencyPoint = (int) (rate / WordsRepo.MAX_FREQUENCY_POINT * 100);
//        Log.d("@@@", results);
//        Log.d("@@@", test.get(0));
//        Log.d("@@@", test2);

        viewHolder.wordNumber.setText(position + 1 + ".");
        viewHolder.wordType.setText(type);
        viewHolder.wordDefinition.setText(definition);

        String exp = "";
        String syn = "";
        for (int i = 0; !examples.isEmpty() && i < examples.size(); i++){
            if (i < examples.size() - 1)
                exp += "\"" + examples.get(i) + "\"" + "\n";
            else
                exp += "\"" + examples.get(i) + "\"";
        }
        for (int i = 0; !synonyms.isEmpty() && i < synonyms.size(); i++){
            if (i < synonyms.size() - 1)
                syn += synonyms.get(i) + ", ";
            else
                syn += synonyms.get(i);
            }

        if (exp != "")
            viewHolder.wordExample.setText(exp);
        else {
            viewHolder.lineExample.setVisibility(View.GONE);
        }
        if (syn != "")
            viewHolder.wordSynonym.setText(syn);
        else{
            viewHolder.lineSynonym.setVisibility(View.GONE);
        }
        //color
        int colorHash = getColor(frequencyPoint);
        viewHolder.labelExample.setTextColor(colorHash);
        viewHolder.labelSynonym.setTextColor(colorHash);
        return convertView;

    }

    static class ViewHolder {
        TextView wordNumber;
        TextView wordType;
        TextView wordDefinition;
        TextView wordExample;
        TextView wordSynonym;
        TextView labelExample;
        TextView labelSynonym;
        RelativeLayout lineExample;
        RelativeLayout lineSynonym;
    }
}
